package team.mosk.api.server.global.security.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.mosk.api.server.domain.store.model.persist.Store;

import java.time.LocalDate;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private Long id;
    private String email;
    private String password;

    private LocalDate period;

    public CustomUserDetails(Long id, String email, String password, LocalDate period) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.period = period;
    }

    public CustomUserDetails(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getPeriod() {return period; }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static CustomUserDetails of(final Store store) {
        return new CustomUserDetails(store.getId(), store.getEmail());
    }
}
