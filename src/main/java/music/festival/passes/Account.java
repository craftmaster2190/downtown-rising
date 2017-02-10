package music.festival.passes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import music.festival.CommonEntity;
import org.hashids.Hashids;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Created by bryce_fisher on 1/4/17.
 */
@Entity
public class Account extends CommonEntity {

    @Transient
    private static final int BADGE_ID_LENGTH = 8;
    /**
     * Obfuscate badge ids so they are not in sequence.
     * Just using the database record id because the database will ensure uniqueness.
     */
    @Transient
    private static final Hashids HASHIDS = new Hashids("DowntownRising", BADGE_ID_LENGTH, "0123456789ABCDEF");
    private Long cityWeeklyTicketId;
    private String wristbandBadgeId;
    private String ticketType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;
    private Date birthDate;
    private String gender;
    private String phone;
    private String phoneType;
    private String email;
    private String heardAbout;
    private String genrePreferences;
    private String deliveryPreference;

    public String getDeliveryPreference() {
        return deliveryPreference;
    }

    public void setDeliveryPreference(final String deliveryPreference) {
        this.deliveryPreference = deliveryPreference;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Transient
    @JsonIgnore
    public String getMiddleInitial() {
        if (StringUtils.hasText(middleName)) {
            return middleName.substring(0, 1);
        }
        return null;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(final String zip) {
        this.zip = zip;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(final String phoneType) {
        this.phoneType = phoneType;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(final Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getHeardAbout() {
        return heardAbout;
    }

    public void setHeardAbout(final String heardAbout) {
        this.heardAbout = heardAbout;
    }

    public String getGenrePreferences() {
        return genrePreferences;
    }

    public void setGenrePreferences(final String genrePreferences) {
        this.genrePreferences = genrePreferences;
    }

    @Column(unique = true)
    public Long getCityWeeklyTicketId() {
        return cityWeeklyTicketId;
    }

    public void setCityWeeklyTicketId(final Long cityWeeklyTicketId) {
        this.cityWeeklyTicketId = cityWeeklyTicketId;
        refreshBadgeId();
    }

    /**
     * HashIds will generate the same hashid every time for the same ID.
     * But to people the Badge ID will be out of order and random prevent a person from
     * generating a forged pass based on a sequence id.
     * <p>
     * This is not a security scheme, this an obfuscation scheme.
     * Anyone that wanted to and knew what they were doing could reverse engineer the ID.
     * All we are doing here is making it difficult for the layman to forge a badge.
     * <p>
     * Tested with 100000 fake passes and no violation of uniqueness.
     */
    private void refreshBadgeId() {
        if (cityWeeklyTicketId != null) {
            String badgeId = HASHIDS.encode(cityWeeklyTicketId);
            if (badgeId.length() > BADGE_ID_LENGTH) {
                badgeId = badgeId.substring(0, BADGE_ID_LENGTH - 1);
            }
            setWristbandBadgeId(badgeId);
        }
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getWristbandBadgeId() {
        return wristbandBadgeId;
    }

    public void setWristbandBadgeId(final String wristbandBadgeId) {
        this.wristbandBadgeId = wristbandBadgeId;
        if (wristbandBadgeId == null) {
            refreshBadgeId();
        }
    }

    @Override
    public void setId(final Long id) {
        super.setId(id);
        refreshBadgeId();
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(final String ticketType) {
        this.ticketType = ticketType;
    }

}
