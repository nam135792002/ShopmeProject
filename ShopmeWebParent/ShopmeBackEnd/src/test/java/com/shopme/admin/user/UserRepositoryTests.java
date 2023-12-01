package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userNam = new User("phuongnama32112002@gmail.com","nam2002","Nam","Nguyen Phuong");
		userNam.addRole(roleAdmin);

		User savedUser = repo.save(userNam);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserTwoRoles() {
		User userThanh = new User("thanhking@gmail.com","thanhking2002","Thanh","Le Tu");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		userThanh.addRole(roleEditor);
		userThanh.addRole(roleAssistant);

		User savedUser = repo.save(userThanh);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUser(){
		Iterable<User> listUser = repo.findAll();
		listUser.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById(){
		User userNam = repo.findById(1).get();
		System.out.println(userNam);
//		assertThat(userNam).isNotNull();
	}

	@Test
	public  void testUpdateUser(){
		User userNam = repo.findById(1).get();
		userNam.setEnabled(true);
		userNam.setEmail("namphuong@gmail.com");

		repo.save(userNam);
	}

	@Test
	public void testUpdateUserRoles(){
		User userThanh = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);

		userThanh.getRoles().remove(roleEditor);
		userThanh.addRole(roleSalesPerson);

		repo.save(userThanh);
	}

	@Test
	public void testDeleteUser(){
		Integer userId = 2;
		repo.deleteById(userId);

	}

	@Test
	public void testGetUserByEmail(){
		String email = "namphuong@gmail.com";
		User user = repo.getUserByEmail(email);

		assertThat(user).isNotNull();
	}

	@Test
	public void testCountById(){
		Integer id = 1;
		Long countById = repo.countById(id);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

	@Test
	public void testDisableUser(){
		Integer id = 1;
		repo.updateEnabledStatus(id,false);
	}

	@Test
	public void testEnableUser(){
		Integer id = 3;
		repo.updateEnabledStatus(id,true);
	}

	@Test
	public void testListFirstPage(){
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> page = repo.findAll(pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(user -> System.out.println(user));

		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser(){
		String keyword = "bruce";
		int pageNumber = 0;
		int pageSize = 4;
		Pageable pageable = PageRequest.of(pageNumber,pageSize);
		Page<User> page = repo.findAll(keyword, pageable);

		List<User> listUsers = page.getContent();

		listUsers.forEach(System.out::println);

		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
