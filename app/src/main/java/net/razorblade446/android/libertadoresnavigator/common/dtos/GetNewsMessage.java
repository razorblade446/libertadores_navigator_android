package net.razorblade446.android.libertadoresnavigator.common.dtos;

public class GetNewsMessage {
    public final long lastPolled;

    public GetNewsMessage(long lastPolled) {
        this.lastPolled = lastPolled;
    }
}
