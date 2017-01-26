package music.festival.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.file.ImageEntity;
import music.festival.passes.Pass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by bryce_fisher on 1/4/17.
 */
@Entity
public class Account extends ImageEntity implements UserDetails {
    private String password;
    private List<Role> roles = new ArrayList<>();
    private List<Pass> passes = new ArrayList<>();
    private Date birthdate;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String heardAbout;
    private String genrePreferences;
    private String clientSettings;

    @Transient
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    @ManyToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Lob
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    @JsonIgnore
    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public String toString() {
        if (getUsername() != null)
            return getUsername();
        return super.toString();
    }

    @Lob
    public String getHeardAbout() {
        return heardAbout;
    }

    public void setHeardAbout(String heardAbout) {
        this.heardAbout = heardAbout;
    }

    //Expected to be a JSON string
    @Lob
    public String getClientSettings() {
        return clientSettings;
    }

    public void setClientSettings(String clientSettings) {
        this.clientSettings = clientSettings;
    }

    public String getGenrePreferences() {
        return genrePreferences;
    }

    public void setGenrePreferences(String genrePreferences) {
        this.genrePreferences = genrePreferences;
    }
}
