package dev.danvega.workshop.multimodal.audio;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AudioGeneration {

    private final OpenAiAudioSpeechModel audioSpeechModel;

    public AudioGeneration(OpenAiAudioSpeechModel audioSpeechModel) {
        this.audioSpeechModel = audioSpeechModel;
    }

    @GetMapping("/speak")
    public ResponseEntity<byte[]> generateSpeech(
            @RequestParam(defaultValue = "It's a great time to be a Java & Spring Developer!") String text) {

        var options = OpenAiAudioSpeechOptions.builder()
                .model("tts-1-hd") // or "tts-1-hd" for higher quality
                .voice(OpenAiAudioApi.SpeechRequest.Voice.ALLOY) // ALLOY, ECHO, FABLE, ONYX, NOVA, SHIMMER
                .responseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .speed(1.0) // 0.25 to 4.0
                .build();

        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(text, options);
        TextToSpeechResponse response = audioSpeechModel.call(speechPrompt);

        byte[] audioBytes = response.getResult().getOutput();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"speech.mp3\"")
                .body(audioBytes);
    }

}
