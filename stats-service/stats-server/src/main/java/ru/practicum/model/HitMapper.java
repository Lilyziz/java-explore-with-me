package ru.practicum.model;

public class HitMapper {
    public static Hit toModel(EndpointHit endpointHit) {
        Hit hit = new Hit();
        hit.setApp(endpointHit.getApp());
        hit.setUri(endpointHit.getUri());
        hit.setTimestamp(endpointHit.getTimestamp());
        hit.setIp(endpointHit.getIp());
        return hit;
    }

    public static ViewHits toDto(HitForResponse hitForResponse) {
        ViewHits viewHits = new ViewHits();
        viewHits.setApp(hitForResponse.getApp());
        viewHits.setHits(hitForResponse.getHits());
        viewHits.setUri(hitForResponse.getUri());
        return viewHits;
    }
}
