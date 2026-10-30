package dev.danvega.workshop.multimodal.image;

import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class ImageGeneration {

    private final OpenAiImageModel imageModel;

    public ImageGeneration(OpenAiImageModel imageModel) {
        this.imageModel = imageModel;
    }

    /**
     * The gpt-image models return the image as base64 instead of a URL, so this
     * endpoint decodes it and serves the PNG directly. Open it in a browser.
     * gpt-image-1-mini keeps a live demo fast; swap the model for higher quality.
     */
    @GetMapping(value = "/generate-image", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateImage(
            @RequestParam(defaultValue = "A beautiful sunset over mountains") String prompt) {

        ImageOptions options = OpenAiImageOptions.builder()
                .model("gpt-image-1-mini")
                .width(1024)
                .height(1024)
                .build();

        ImageResponse response = imageModel.call(new ImagePrompt(prompt, options));
        return Base64.getDecoder().decode(response.getResult().getOutput().getB64Json());
    }

}
