package com.shopme.admin.setting.state;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired private StateRepository stateRepository;

    @Autowired private TestEntityManager entityManager;

    @Test
    public void testCreateStatesInVietNam(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class,countryId);
        State state_1 = stateRepository.save(new State("Ha Noi",country));
        State state_2 = stateRepository.save(new State("Ho Chi Minh",country));
        State state_3 = stateRepository.save(new State("Binh Dinh",country));
        State state_4 = stateRepository.save(new State("Thai Binh",country));
        State state_5 = stateRepository.save(new State("Ca Mau",country));
        Assertions.assertThat(state_1).isNotNull();
        Assertions.assertThat(state_1.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateStatesInIndia(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class,countryId);
        State state_1 = stateRepository.save(new State("California",country));
        State state_2 = stateRepository.save(new State("Texas",country));
        State state_3 = stateRepository.save(new State("New York",country));
        State state_4 = stateRepository.save(new State("Washington",country));
        State state_5 = stateRepository.save(new State("Ohio",country));
        Assertions.assertThat(state_1).isNotNull();
        Assertions.assertThat(state_1.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry(){
        Integer countryId = 2;
        Country country = entityManager.find(Country.class,countryId);
        List<State> listStates = stateRepository.findByCountryOrderByNameAsc(country);
        listStates.forEach(System.out::println);

        Assertions.assertThat(listStates.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateState(){
        Integer stateId = 3;
        String stateName = "Binh Thuan";
        State state = stateRepository.findById(stateId).get();
        state.setName(stateName);
        State updatedState = stateRepository.save(state);
        Assertions.assertThat(updatedState.getName()).isEqualTo(stateName);
    }

    @Test
    public void testGetState(){
        Integer stateId = 1;
        Optional<State> findById = stateRepository.findById(stateId);
        Assertions.assertThat(findById).isNotNull();
    }

    @Test
    public void testDeleteState(){
        Integer stateId = 8;
        stateRepository.deleteById(stateId);
        Optional<State> findById = stateRepository.findById(stateId);
        Assertions.assertThat(findById).isEmpty();
    }
}
