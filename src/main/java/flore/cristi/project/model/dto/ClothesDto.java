package flore.cristi.project.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ClothesDto {
    private Long id;
    @NotBlank(message = "Color can't be left empty.")
    private String color;
    @NotNull(message = "Id can't be left empty")
    private Long id1;
}
