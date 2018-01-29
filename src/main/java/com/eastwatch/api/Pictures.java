package com.eastwatch.api;

import com.eastwatch.api.Entity.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.glassfish.jersey.client.ClientResponse;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

import static com.eastwatch.api.Serialization.gsonDeserialization;
import static com.eastwatch.api.Serialization.gsonSerialization;

/**
 * Created by Jonelezhang on 1/14/18.
 */
@Path("/images")
public class Pictures extends JedisConnection {

    private JedisPool jedisPool;

    private Client client1;

    private Client client2;

    public Pictures() {
        jedisPool = JedisConnection.getInstance();
    }

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "Application_JSON"
    @Produces({MediaType.APPLICATION_JSON})

    public EpisodeImageList getAllEpisodePics(@QueryParam("tv") int tv, @QueryParam("season") int season) {

        //api request get episode count info
        client1 = ClientBuilder.newClient();
        client2 = ClientBuilder.newClient();
        TmdbInfo tmdbInfo = apiTmdbInfo(tv, season);
        String[] result = new String[tmdbInfo.getEpisodeCount()];
        List<Integer> noCacheImageList = new ArrayList<>();
        EpisodeImageList episodeImageList;

        try (Jedis jedis = jedisPool.getResource()) {
            // check if jedis have all episodes image info
            // jedis  Hashes structure "{tv}#{season}#{episode},  imageJsonResult"
            for (int i = 1; i <= tmdbInfo.getEpisodeCount(); i++) {
                String key = Integer.toString(tmdbInfo.getTv()) + "#" + Integer.toString(tmdbInfo.getSeason()) + "#" + Integer.toString(i);
                Boolean exist = jedis.exists(key);
                if (exist) {
                    result[i - 1] = jedis.get(key);
                } else {
                    noCacheImageList.add(i);
                }
            }

            if (noCacheImageList.size() > 0) {
                Map<Integer, Map<String, String>> apiResult = appImagesList(tmdbInfo, noCacheImageList);
                for (int i : noCacheImageList) {
                    String key = Integer.toString(tmdbInfo.getTv()) + "#" + Integer.toString(tmdbInfo.getSeason()) + "#" + Integer.toString(i);
                    result[i - 1] = apiResult.get(i).get(key);
                    jedis.set(key, result[i - 1]);
                }
            }

            List<EpisodeImage> episodeImages = new ArrayList<EpisodeImage>();
            for (String s : result) {
                episodeImages.add(gsonDeserialization(new EpisodeImage(), s));
            }

            episodeImageList = new EpisodeImageList(tmdbInfo.getTv(), tmdbInfo.getTmdb(), tmdbInfo.getSeason(), episodeImages);
        }
        return episodeImageList;
    }

    /**
     * Api request to get show's tmdb and number of episodes
     */

    public TmdbInfo apiTmdbInfo(int tv, int season){
        //Concurrency
        int tmdb = 0;
        int numberOfEpisodes = 0;
        try {
            int threadNum = 2;
            ExecutorService executor = Executors.newFixedThreadPool(threadNum);
            List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();

            // Start thread for the first half of the numbers
            FutureTask<Integer> futureTask_1 = new FutureTask<Integer>(new Callable<Integer>() {
                @Override
                public Integer call() {
                    //Api request to get shows info
                    ShowInfo showResponse = client1.target("https://api.trakt.tv/shows/" + tv)
                            .request(MediaType.APPLICATION_JSON)
                            .header("trakt-api-version", 2)
                            .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                            .get(ShowInfo.class);
                    return showResponse.getIds().getTmdb();
                }
            });
            taskList.add(futureTask_1);
            executor.execute(futureTask_1);

            // Start thread for the second half of the numbers
            FutureTask<Integer> futureTask_2 = new FutureTask<Integer>(new Callable<Integer>() {
                @Override
                public Integer call() {
                    //Api requst to get all episodes info in season
                    List<EpisodeInfo> episodeResponse = client2.target("https://api.trakt.tv/shows/" + tv + "/seasons/" + season)
                            .request(MediaType.APPLICATION_JSON)
                            .header("trakt-api-version", 2)
                            .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                            .get(new GenericType<List<EpisodeInfo>>() {
                            });
                    return episodeResponse.size();
                }
            });
            taskList.add(futureTask_2);
            executor.execute(futureTask_2);
            tmdb = taskList.get(0).get();
            numberOfEpisodes = taskList.get(1).get();
            executor.shutdown();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return new TmdbInfo(tv, tmdb, season, numberOfEpisodes);
    }

    /**
     * Api requst to get imgs info for all episode under a season.
     */
    public Map<Integer, Map<String, String>> appImagesList(TmdbInfo tmdbInfo, List<Integer> episodeIds) {
        //Api request for getting each episode image info
        String key = "94ca141ea826a21f5802fc9dae837698";
        Map<Integer, Map<String, String>> result = new HashMap<>();

        for (int i : episodeIds) {
            EpisodeImage imageResponse = client1.target("https://api.themoviedb.org/3/tv/" + tmdbInfo.getTmdb() +
                    "/season/" + tmdbInfo.getSeason() + "/episode/" + i + "/images")
                    .queryParam("api_key", key)
                    .request(MediaType.APPLICATION_JSON)
                    .header("trakt-api-version", 2)
                    .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                    .get(EpisodeImage.class);
            imageResponse.setEpisodeId(i);
            String jedisKey = Integer.toString(tmdbInfo.getTv()) + "#" + Integer.toString(tmdbInfo.getSeason()) + "#" + Integer.toString(i);
            Map<String, String> temp = new HashMap<>();
            temp.put(jedisKey, gsonSerialization(imageResponse));
            result.put(i, temp);
        }
        return result;
    }
}
