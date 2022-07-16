package net.fabricmc.wildmod_copper.utils.resource.generators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.devtech.arrp.util.UnsafeByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class JHelper {

  public static final Gson GSON =
    new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .registerTypeAdapter(ToxBlockState.class, new ToxBlockState.Serializer())
                .create();

  public static byte[] serialize(Object object) {
    UnsafeByteArrayOutputStream ubaos = new UnsafeByteArrayOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(ubaos, StandardCharsets.UTF_8);
    GSON.toJson(object, writer);
    try {
      writer.close();
    } catch(IOException e) {
      throw new RuntimeException(e);
    }
    return ubaos.getBytes();
  }
}
