package com.eastwatch.api;

import com.eastwatch.api.Entity.EpisodeImage;
import com.eastwatch.api.Entity.EpisodeInfo;
import com.eastwatch.api.Entity.ShowInfo;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonelezhang on 1/14/18.
 */
@Path("/images")
public class Pictures extends JedisConnection{

    private JedisPool jedisPool;
    public Pictures(){
        jedisPool = JedisConnection.getInstance();
    }


    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "Application_JSON"
    @Produces({MediaType.APPLICATION_JSON})

    public List<EpisodeImage>  getAllEpisodePics(@QueryParam("tv") int tv, @QueryParam("season") int season) {

        try (Jedis jedis = jedisPool.getResource()) {
            // do operations with jedis resource
        }

        //check if we save the number of episodes in Jedis, if not do api request


        //Api request to get shows info
        Client client = ClientBuilder.newClient();
        ShowInfo showResponse = client.target("https://api.trakt.tv/shows/" + tv)
                .request(MediaType.APPLICATION_JSON)
                .header("trakt-api-version", 2)
                .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                .get(ShowInfo.class);


        //Api requst to get all episodes info in season
        List<EpisodeInfo> episodeResponse = client.target("https://api.trakt.tv/shows/" + tv + "/seasons/" + season)
                .request(MediaType.APPLICATION_JSON)
                .header("trakt-api-version", 2)
                .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                .get(new GenericType<List<EpisodeInfo>>() {
                });


        //Api request for getting each episode image info
        String key = "94ca141ea826a21f5802fc9dae837698";
        List<EpisodeImage> episodeImages = new ArrayList<>();
        int numberOfEpisodes = episodeResponse.size();

        for (int i = 1; i <= numberOfEpisodes; i++) {
            EpisodeImage imageResponse = client.target("https://api.themoviedb.org/3/tv/" + showResponse.getIds().getTmdb() +
                    "/season/" + season + "/episode/" + i + "/images")
                    .queryParam("api_key", key)
                    .request(MediaType.APPLICATION_JSON)
                    .header("trakt-api-version", 2)
                    .header("trakt-api-key", "3acd1e398e8b01e184c6e0b40a21706db046194e976cdc5ba8237d552ee64fc6")
                    .get(EpisodeImage.class);
                    episodeImages.add(imageResponse);
        }



            return episodeImages;
        }


    }
