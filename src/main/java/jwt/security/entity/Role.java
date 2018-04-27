package jwt.security.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

	private static final String ROLE_PREFIX = "ROLE_";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleType roleType;

	public Role() {

	}

	public Role(RoleType role) {
		this.roleType = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return ROLE_PREFIX + this.roleType.toString();
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
}
