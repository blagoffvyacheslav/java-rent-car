package integration.com.dmdev.entity;

import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_MODEL_ID = 2L;
    public static final Long TEST_MODEL_ID_FOR_DELETE = 1L;

    public static Model getExistModel() {
        return Model.builder()
                .id(2L)
                .name("Volvo X70")
                .build();
    }


    public static Model createModel() {
        return Model.builder()
                .name("Kia Seed")
                .build();
    }

    @Test
    public void shouldCreateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToSave = createModel();

            Long savedModelId = (Long) session.save(modelToSave);
            session.getTransaction().commit();

            assertThat(savedModelId).isNotNull();
        }
    }

    @Test
    public void shouldReturnModel() {
        try (Session session = sessionFactory.openSession()) {
            Model expectedModel = getExistModel();

            Model actualModel = session.find(Model.class, TEST_EXISTS_MODEL_ID);

            assertThat(actualModel).isNotNull();
            assertEquals(expectedModel, actualModel);
        }
    }

    @Test
    public void shouldUpdateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToUpdate = session.find(Model.class, TEST_EXISTS_MODEL_ID);

            session.update(modelToUpdate);
            session.flush();
            session.evict(modelToUpdate);

            Model updatedModel = session.find(Model.class, modelToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedModel).isEqualTo(modelToUpdate);
        }
    }

    @Test
    public void shouldDeleteModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToDelete = session.find(Model.class, TEST_MODEL_ID_FOR_DELETE);

            session.delete(modelToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Model.class, modelToDelete.getId())).isNull();
        }
    }
}