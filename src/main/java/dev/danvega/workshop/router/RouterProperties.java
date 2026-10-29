package dev.danvega.workshop.router;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("router")
public record RouterProperties(String fallback, double confidenceThreshold, List<Model> models) {

    public RouterProperties {
        models = models == null ? List.of() : List.copyOf(models);
        if (models.isEmpty()) {
            throw new IllegalArgumentException("router.models must list at least one model");
        }
        if (models.stream().map(Model::id).distinct().count() != models.size()) {
            throw new IllegalArgumentException("router.models must have unique IDs");
        }
        if (models.stream().noneMatch(model -> model.id().equals(fallback))) {
            throw new IllegalArgumentException("router.fallback must identify a configured model");
        }
    }

    public Model model(String id) {
        return models.stream().filter(model -> model.id().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown model route"));
    }

    public enum Tier { ECONOMY, BALANCED, PREMIUM }

    public record Model(String id, String name, Tier tier, String description) {
    }
}
