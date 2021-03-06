package com.github.anddd7.security.repository

import com.github.anddd7.SQLScript
import com.github.anddd7.security.model.PermissionCode
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureEmbeddedDatabase
@Sql(scripts = [SQLScript.AUTH_USER, SQLScript.AUTH_ROLE, SQLScript.AUTH_PERMISSION])
internal class AuthUserPermissionRepositoryTest {
  @Autowired
  private lateinit var authUserRepository: AuthUserRepository

  @Test
  fun `should return whether role have the permission`() {
    val user2 = authUserRepository.getOne(2)
    assertThat(user2.permissions.any { it.belong(PermissionCode.DASHBOARD) }).isTrue()
    assertThat(user2.permissions.any { it.belong(PermissionCode.ORDER) }).isFalse()

    val user3 = authUserRepository.getOne(3)
    assertThat(user3.permissions.any { it.belong(PermissionCode.DASHBOARD) }).isTrue()
    assertThat(user3.permissions.any { it.belong(PermissionCode.ORDER) }).isFalse()

    val user4 = authUserRepository.getOne(4)
    assertThat(user4.permissions.any { it.belong(PermissionCode.DASHBOARD) }).isTrue()
    assertThat(user4.permissions.any { it.belong(PermissionCode.ORDER) }).isTrue()
  }

  @Autowired
  private lateinit var authPermissionRepository: AuthPermissionRepository

  @Test
  fun `should allowed users of the permission`() {
    val dashboard = authPermissionRepository.getOne(PermissionCode.DASHBOARD.name)
    assertThat(dashboard.roles).hasSize(3)
    assertThat(dashboard.users).hasSize(4)

    val order = authPermissionRepository.getOne(PermissionCode.ORDER.name)
    assertThat(order.roles).hasSize(1)
    assertThat(order.users).hasSize(1)
  }
}
