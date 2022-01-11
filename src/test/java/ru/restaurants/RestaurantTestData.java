package ru.restaurants;

import ru.restaurants.model.Restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.restaurants.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);
    public static MatcherFactory.Matcher<Restaurant> RESTAURANT_WITH_MEALS_MATCHER =
            MatcherFactory.usingAssertions(Restaurant.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison().ignoringFields("meals","choices").isEqualTo(e),
                    (a, e) ->
                        assertThat(a).usingRecursiveComparison().ignoringFields("meals","choices").isEqualTo(e)
                    );
    public static final int RESTAURANT_1_ID = START_SEQ;
    public static final int RESTAURANT_2_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "Alchemist");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, "Direkte");

    public static Restaurant getNew() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(restaurant1);
        updated.setName("UpdatedName");
        return updated;
    }
}
