package integration.com.dmdev.service;

import com.dmdev.dto.ModelReadDto;
import com.dmdev.service.ModelService;

import integration.com.dmdev.IntegrationBaseTest;

import utils.builder.ModelBuilder;
import utils.builder.TestDtoBuilder;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class ModelServiceTestIT extends IntegrationBaseTest {

    private final ModelService modelService;

    @Test
    void shouldSaveModelCorrectly() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();

        var actualModel = modelService.create(modelCreateDto);

        assertEquals(modelCreateDto.getName(), actualModel.getName());
    }

    @Test
    void shouldFindAllModels() {
        var models = modelService.getAll();

        assertThat(models).hasSize(2);

        var names = models.stream().map(ModelReadDto::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Kia Rio", "Volvo X70");
    }

    @Test
    void shouldReturnModelById() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var expectedModel = modelService.create(modelCreateDto);

        var actualModel = modelService.getById(expectedModel.getId());

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
    }

    @Test
    void shouldUpdateCarCorrectly() {
        var modelCreateDto = TestDtoBuilder.createModelCreateDTO();
        var savedModel = modelService.create(modelCreateDto);

        var modelUpdateDto = TestDtoBuilder.createModelUpdateDTO();
        var actualModel = modelService.update(savedModel.getId(), modelUpdateDto);

        assertThat(actualModel).isNotNull();
    }

    @Test
    void shouldDeleteCarByIdCorrectly() {
        assertTrue(modelService.deleteById(ModelBuilder.TEST_MODEL_ID_FOR_DELETE));
    }
}