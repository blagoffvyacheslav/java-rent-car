package integration.com.dmdev.entity;

import com.dmdev.entity.Damage;
import integration.com.dmdev.IntegrationBaseTest;
import java.math.BigDecimal;

public class DamageTestIT extends IntegrationBaseTest {

    public static final Long TEST_EXISTS_DAMAGE_ID = 2L;
    public static final Long TEST_DAMAGE_ID_FOR_DELETE = 1L;

    public static Damage getExistDamage() {
        return Damage.builder()
                .id(TEST_EXISTS_DAMAGE_ID)
                .order(OrderTestIT.getExistOrder())
                .description("broken headlight")
                .amount(BigDecimal.valueOf(50.00).setScale(2))
                .build();
    }

    public static Damage createDamage() {
        return Damage.builder()
                .order(OrderTestIT.getExistOrder())
                .amount(BigDecimal.valueOf(99.99))
                .description("damage test description saved")
                .build();
    }
}