package integration.com.dmdev.repository;

import com.dmdev.entity.Damage;
import com.dmdev.entity.Car;
import com.dmdev.entity.Order;
import com.dmdev.repository.DamageRepository;
import com.dmdev.repository.OrderRepository;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.entity.DamageTestIT;
import integration.com.dmdev.entity.OrderTestIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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
        var order = orderRepository.findById(OrderTestIT.TEST_EXISTS_ORDER_ID).get();
        var damageToSave = DamageTestIT.createDamage();
        order.setDamage(damageToSave);

        var savedDamage = damageRepository.saveAndFlush(damageToSave);

        assertNotNull(savedDamage.getId());
    }

    @Test
    void shouldFindByIdDamage() {
        var expectedDamage = Optional.of(DamageTestIT.getExistDamage());

        var actualDamage = damageRepository.findById(DamageTestIT.TEST_EXISTS_DAMAGE_ID);

        assertThat(actualDamage).isNotNull();
        assertEquals(expectedDamage, actualDamage);
    }

    @Test
    void shouldUpdateDamage() {
        var damageToUpdate = damageRepository.findById(DamageTestIT.TEST_EXISTS_DAMAGE_ID).get();
        var existOrder = orderRepository.findById(OrderTestIT.TEST_EXISTS_ORDER_ID).get();
        damageToUpdate.setAmount(BigDecimal.valueOf(3456.76));
        damageToUpdate.setDescription("test description");
        damageToUpdate.setOrder(existOrder);

        damageRepository.saveAndFlush(damageToUpdate);

        var updatedDamage = damageRepository.findById(damageToUpdate.getId()).get();
        assertThat(updatedDamage).isEqualTo(damageToUpdate);
    }

    @Test
    void shouldDeleteDamage() {
        var damage = damageRepository.findById(DamageTestIT.TEST_DAMAGE_ID_FOR_DELETE);

        damage.ifPresent(dmg -> damageRepository.delete(dmg));

        assertThat(damageRepository.findById(DamageTestIT.TEST_DAMAGE_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllDamages() {
        List<Damage> damages = damageRepository.findAll();
        assertThat(damages).hasSize(2);

        List<BigDecimal> damage = damages.stream().map(Damage::getAmount).collect(toList());
        assertThat(damage).containsExactlyInAnyOrder(
                BigDecimal.valueOf(100.00).setScale(2), BigDecimal.valueOf(50.00).setScale(2));
    }

    @Test
    void shouldFindAllDamagesSortedByOrderAndAmount() {
        Sort sort = Sort.by("order").descending().and(Sort.by("amount")).descending();
        List<Damage> damages = damageRepository.findAll(sort);

        assertThat(damages).hasSize(2);

        List<Long> orderIds = damages.stream().map(Damage::getOrder).map(Order::getId).collect(toList());
        assertThat(orderIds).containsExactly(
                2L, 1L);

        List<BigDecimal> damage = damages.stream().map(Damage::getAmount).collect(toList());
        assertThat(damage).containsExactly(
                BigDecimal.valueOf(50.00).setScale(2), BigDecimal.valueOf(100.00).setScale(2));
    }

    @Test
    void shouldFindAllDamagesByOrderId() {
        List<Damage> damages = damageRepository.findAllByOrderId(OrderTestIT.TEST_EXISTS_ORDER_ID);

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0)).isEqualTo(DamageTestIT.getExistDamage());
    }

    @Test
    void shouldFindAllDamagesByNameAndLastname() {
        List<Damage> damages = damageRepository.findAllByNameAndLastname("Vyacheslav", "Blagov");

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0)).isEqualTo(DamageTestIT.getExistDamage());
    }

    @Test
    void shouldFindAllDamagesBySerialNumber() {
        List<Damage> damages = damageRepository.findAllBySerialNumber("0123456");
        assertThat(damages).hasSize(1);
        List<String> carSerialNumbers = damages.stream()
                .map(Damage::getOrder)
                .map(Order::getCar)
                .map(Car::getSerialNumber)
                .collect(toList());

        assertThat(damages).hasSize(1);
        assertThat(carSerialNumbers).containsExactlyInAnyOrder("0123456");
    }

    @Test
    void shouldReturnDamagesByMoreAvgAmount() {
        List<Damage> damages = damageRepository.findAllByAvgAmountMore();

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2));
    }

    @Test
    void shouldReturnDamagesByMoreAmount() {
        List<Damage> damages = damageRepository.findAllByAmount(BigDecimal.valueOf(100.00).setScale(2));

        assertThat(damages).hasSize(1);
        assertThat(damages.get(0).getAmount()).isEqualTo(BigDecimal.valueOf(100.00).setScale(2));
    }
}