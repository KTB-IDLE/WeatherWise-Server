package com.idle.weather.mission.domain;

public enum WeatherResponseType {
    T1H("기온", "℃"),
    RN1("1시간 강수량", "mm"),
    SKY("하늘상태", "코드값"),
    UUU("동서바람성분", "m/s"),
    VVV("남북바람성분", "m/s"),
    REH("습도", "%"),
    PTY("강수형태", "코드값"),
    LGT("낙뢰", "kA"),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s");
    private final String description;
    private final String unit;
    WeatherResponseType(String description, String unit) {
        this.description = description;
        this.unit = unit;
    }
    public String getDescription() {
        return description;
    }
    public String getUnit() {
        return unit;
    }
}
