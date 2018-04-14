package com.aurawin.scs.media;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MP3Details {
    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3dn")
    public long Duration;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3br")
    public double Bitrate;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3sr")
    public int Samplerate;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3tr")
    public String Track;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3ar")
    public String Artist;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3tl")
    public String Title;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3al")
    public String Album;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3yr")
    public String Year;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3gr")
    public int Genre;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3gd")
    public String GenreDescription;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3ct")
    public String Comment;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3cm")
    public String Composer;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3pb")
    public String Publisher;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3oa")
    public String OriginalArtist;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3aa")
    public String AlbumArtist;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3cr")
    public String Copyright;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3ul")
    public String Url;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("m3en")
    public String Encoder;
}
