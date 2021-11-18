package com.app.authentication.security;

import com.app.authentication.models.UserModel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDetailsImpl implements UserDetails {

  private String email;
  private String password;
  private Collection<? extends GrantedAuthority> authorities;

  public static UserDetailsImpl build(UserModel userModel){
    List<GrantedAuthority> authorities = userModel.getRoles().stream()
            .map(rol -> new SimpleGrantedAuthority(rol.getRolName().name())).collect(Collectors.toList());
    return new UserDetailsImpl(userModel.getEmail(), userModel.getPassword(), authorities);
  }


  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

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

}
