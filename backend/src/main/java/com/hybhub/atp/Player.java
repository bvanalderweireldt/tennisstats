package com.hybhub.atp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hybhub.atp.enumeration.Backhand;
import com.hybhub.atp.enumeration.Handedness;

import java.time.LocalDate;

public interface Player {

    @JsonProperty
    void setId(final String id);

    @JsonProperty
    String getId();

    @JsonProperty
    void setName(final String name);

    @JsonProperty
    String getName();

    @JsonProperty
    String getCountry();

    @JsonProperty
    void setCountry(final String country);

    @JsonIgnore
    int getAge();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty
    LocalDate getDob();

    @JsonProperty
    void setDob(final LocalDate age);

    @JsonProperty
    Handedness getHandedness();

    @JsonProperty
    void setHandedness(final Handedness handedness);

    @JsonProperty
    Backhand getBackhand();

    @JsonProperty
    void setBackhand(final Backhand backhand);

    @JsonProperty
    int getWeight();

    @JsonProperty
    void setWeight(final int weight);

    @JsonProperty
    int getHeight();

    @JsonProperty
    void setHeight(final int height);

}
