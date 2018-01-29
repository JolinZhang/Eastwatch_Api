package com.eastwatch.api;

import com.eastwatch.api.Entity.EpisodeImage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;

/**
 * Created by Jonelezhang on 1/27/18.
 */
public class Serialization {

    /**
     * Use Gson library to serialize an object to string
     */

    public static <T>String gsonSerialization(T myObject){
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        String json = gson.toJson(myObject, type);
        return json;
    }

    /**
     * Use Gson library to deserialize a string to object
     * */
    public static <T> T gsonDeserialization(T myobject, String json){
        Gson gson = new Gson();
        Type type = new TypeToken<T>() {}.getType();
        T fromJson = gson.fromJson(json, type);
        return fromJson;
    }

    /**
     * serialize of an object to string
     * */
    public static String serialization(EpisodeImage myObject){
        String serializedObject = "";

        // serialize the object
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(myObject);
            so.flush();
            serializedObject = bo.toString("ISO-8859-1");
        } catch (Exception e) {
            System.out.println(e);
        }
        return serializedObject;
    }

    /**
     * Descrialization of a string to an object
     * */
    public static <T> T deserialization(String serializedObject){
        T obj = null;
        try {
            byte b[] = serializedObject.getBytes("ISO-8859-1");
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            obj = (T) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return obj;
    }

}
