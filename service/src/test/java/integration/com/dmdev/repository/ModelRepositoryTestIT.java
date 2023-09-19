package integration.com.dmdev.repository;

import com.dmdev.entity.Model;
import com.dmdev.repository.ModelRepository;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.CarBuilder;
import utils.builder.ModelBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private ModelRepository modelRepository;
    @Test
    void shouldSaveModel() {
        var modelToSave = ModelBuilder.createModel();

        var savedModel = modelRepository.saveAndFlush(modelToSave);

        assertThat(savedModel).isNotNull();
    }

    @Test
    void shouldCreateModelWithNotExistsCar() {
        var carToSave = CarBuilder.createCar();
        var modelToSave = ModelBuilder.createModel();
        carToSave.setModel(modelToSave);

        modelRepository.saveAndFlush(modelToSave);

        assertThat(modelToSave.getId()).isNotNull();
        assertThat(carToSave.getId()).isNotNull();
        assertThat(modelToSave.getCars()).contains(carToSave);
        assertThat(carToSave.getModel().getId()).isEqualTo(modelToSave.getId());
    }

    @Test
    void shouldFindByIdModel() {
        var expectedModel = Optional.of(ModelBuilder.getExistModel());

        var actualModel = modelRepository.findById(ModelBuilder.TEST_EXISTS_MODEL_ID);

        assertThat(actualModel).isNotNull();
        assertEquals(expectedModel, actualModel);
    }

    @Test
    void shouldUpdateModel() {
        var modelToUpdate = modelRepository.findById(ModelBuilder.TEST_EXISTS_MODEL_ID).get();

        modelRepository.saveAndFlush(modelToUpdate);

        var updatedModel = modelRepository.findById(modelToUpdate.getId()).get();

        assertThat(updatedModel).isEqualTo(modelToUpdate);
    }

    @Test
    void shouldDeleteModel() {
        var model = modelRepository.findById(ModelBuilder.TEST_MODEL_ID_FOR_DELETE);

        model.ifPresent(md -> modelRepository.delete(md));

        assertThat(modelRepository.findById(ModelBuilder.TEST_MODEL_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllModels() {
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(2);

        List<String> names = models.stream().map(Model::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Kia Rio", "Volvo X70");
    }

    @Test
    void shouldFindAllModelsByName() {
        List<Model> models = modelRepository.findModelsByName("Volvo X50");
        assertThat(models).hasSize(0);

    }
}