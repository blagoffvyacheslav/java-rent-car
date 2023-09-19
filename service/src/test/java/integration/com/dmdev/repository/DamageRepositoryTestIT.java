package integration.com.dmdev.repository;

import com.dmdev.entity.Car;
import com.dmdev.repository.DamageRepository;
import com.dmdev.repository.OrderRepository;
import integration.com.dmdev.IntegrationBaseTest;
import utils.builder.DamageBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import utils.builder.OrderBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DamageRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private DamageRepository damageRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveDamage() {
        var order = orderRepository.findById(OrderBuilder.TEST_EXISTS_ORDER_ID).get();
        var damageToSave = DamageBuilder.createDamage();
        order.setDamage(damageToSave);

        var savedDamage = damageRepository.saveAndFlush(damageToSave);

        assertNotNull(savedDamage.getId());
    }

    @Test
    void shouldFindByIdDamage() {
        var expectedDamage = Optional.of(DamageBuilder.getExistDamage());

        var actualDamage = damageRepository.findById(DamageBuilder.TEST_EXISTS_DAMAGE_ID);

        assertThat(actualDamage).isNotNull();
        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    void shouldUpdateDamage() {
        var damageToUpdate = damageRepository.findById(DamageBuilder.TEST_EXISTS_DAMAGE_ID).get();
        var existOrder = orderRepository.findById(OrderBuilder.TEST_EXISTS_ORDER_ID).get();
        damageToUpdate.setAmount(BigDecimal.valueOf(3456.76));
        damageToUpdate.setDescription("test description");
        damageToUpdate.setOrder(existOrder);

        damageRepository.saveAndFlush(damageToUpdate);

        var updatedDamage = damageRepository.findById(damageToUpdate.getId()).get();
        assertThat(updatedDamage).isEqualTo(damageToUpdate);
    }

    @Test
    void shouldDeleteDamage() {
        var damage = damageRepository.findById(DamageBuilder.TEST_DAMAGE_ID_FOR_DELETE);

        damage.ifPresent(dmg -> damageRepository.delete(dmg));

        assertThat(damageRepository.findById(DamageBuilder.TEST_DAMAGE_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllDamages() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAll();
        assertThat(damages).hasSize(2);

        List<BigDecimal> damage = damages.stream().map(com.dmdev.entity.Damage::getAmount).collect(toList());
        assertThat(damage).containsExactlyInAnyOrder(
                BigDecimal.valueOf(100.00).setScale(2), BigDecimal.valueOf(50.00).setScale(2));
    }

    @Test
    void shouldFindAllDamagesSortedByOrderAndAmount() {
        Sort sort = Sort.by("order").descending().and(Sort.by("amount")).descending();
        List<com.dmdev.entity.Damage> damages = damageRepository.findAll(sort);

        assertThat(damages).hasSize(2);

        List<Long> orderIds = damages.stream().map(com.dmdev.entity.Damage::getOrder).map(com.dmdev.entity.Order::getId).collect(toList());
        assertThat(orderIds).containsExactly(
                2L, 1L);

        List<BigDecimal> damage = damages.stream().map(com.dmdev.entity.Damage::getAmount).collect(toList());
        assertThat(damage).containsExactly(
                BigDecimal.valueOf(50.00).setScale(2), BigDecimal.valueOf(100.00).setScale(2));
    }

    @Test
    void shouldFindAllDamagesByOrderId() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAllByOrderId(OrderBuilder.TEST_EXISTS_ORDER_ID);

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0)).isEqualTo(DamageBuilder.getExistDamage());
    }

    @Test
    void shouldFindAllDamagesByNameAndLastname() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAllByNameAndLastname("Vyacheslav", "Blagov");

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0)).isEqualTo(DamageBuilder.getExistDamage());
    }

    @Test
    void shouldFindAllDamagesBySerialNumber() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAllBySerialNumber("0123456");
        assertThat(damages).hasSize(1);
        List<String> carSerialNumbers = damages.stream()
                .map(com.dmdev.entity.Damage::getOrder)
                .map(com.dmdev.entity.Order::getCar)
                .map(Car::getSerialNumber)
                .collect(toList());

        assertThat(damages).hasSize(1);
        assertThat(carSerialNumbers).containsExactlyInAnyOrder("0123456");
    }

    @Test
    void shouldReturnDamagesByMoreAvgAmount() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAllByAvgAmountMore();

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2));
    }

    @Test
    void shouldReturnDamagesByMoreAmount() {
        List<com.dmdev.entity.Damage> damages = damageRepository.findAllByAmount(BigDecimal.valueOf(100.00).setScale(2));

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2));
    }
}