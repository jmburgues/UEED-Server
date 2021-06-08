package edu.utn.UEEDServer.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class EntityURLBuilder {

    public static String buildURL(final String entity,final String id)
    {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{entity}/{id}")
                .buildAndExpand(entity,id)
                .toString();
    }


    public static String buildURL(final String entity,final Integer id)
    {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/{entity}/{id}")
                .buildAndExpand(entity,id)
                .toString();
    }

    public static URI buildURL(final String id)
    {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{entity}")
                .buildAndExpand(id)
                .toUri();
    }
    public static URI buildURL(final Integer id)
    {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{entity}")
                .buildAndExpand(id)
                .toUri();
    }

}
