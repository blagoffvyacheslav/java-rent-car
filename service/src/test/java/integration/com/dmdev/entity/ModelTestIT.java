package integration.com.dmdev.entity;

import com.dmdev.entity.Model;
import integration.com.dmdev.IntegrationBaseTest;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTestIT extends IntegrationBaseTest {

    public static Model getExistModel() {
        return Model.builder()
                .id(2L)
                .name("Volvo X70")
                .build();
    }

    public static Model getUpdatedModel() {
        return Model.builder()
                .id(2L)
                .name("Mazda 6")
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
            Long savedBrandId = (Long) session.save(createModel());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedBrandId);
        }
    }

    @Test
    public void shouldReturnModel() {
        try (Session session = sessionFactory.openSession()) {
            Model actualBrand = session.find(Model.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualBrand).isNotNull();
            assertEquals(getExistModel().getName(), actualBrand.getName());
        }
    }

    @Test
    public void shouldUpdateModel() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Model modelToUpdate = getUpdatedModel();
            session.update(modelToUpdate);
            session.getTransaction().commit();

            Model updatedBrand = session.find(Model.class, modelToUpdate.getId());

            assertThat(updatedBrand).isEqualTo(modelToUpdate);
        }
    }

    @Test
    public void shouldDeleteModel() {
        try (Session session = sessionFactory.openSession()) {
            Model brandToDelete = session.find(Model.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(brandToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Model.class, brandToDelete.getId())).isNull();
        }
    }
}