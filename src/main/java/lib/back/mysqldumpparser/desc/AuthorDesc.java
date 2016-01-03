package lib.back.mysqldumpparser.desc;

import lib.back.mysqldumpparser.SkipField;
import lib.back.mysqldumpparser.SortOrder;

import javax.persistence.Id;

/**
 * Created by Alexey on 03.01.2016.
 */
public class AuthorDesc {

        @SortOrder(0)
        Long authorId;

        @SortOrder(1)
        String firstName;

        @SortOrder(2)
        String middleName;

        @SortOrder(3)
        String lastName;

        @SortOrder(4)
        @SkipField
        String nickName;

        @SortOrder(5)
        @SkipField
        Boolean noDonate;

        @SortOrder(6)
        @SkipField
        Long uid;

        @SortOrder(7)
        @SkipField
        String webPay;

        @SortOrder(8)
        @SkipField
        String email;

        @SortOrder(9)
        @SkipField
        String homepage;

        @SortOrder(10)
        @SkipField
        Short source;

        @SortOrder(11)
        @SkipField
        Short state;

        @SortOrder(12)
        @SkipField
        Long sourceId;

        public Long getAuthorId() {
                return authorId;
        }

        public void setAuthorId(Long authorId) {
                this.authorId = authorId;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getMiddleName() {
                return middleName;
        }

        public void setMiddleName(String middleName) {
                this.middleName = middleName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getNickName() {
                return nickName;
        }

        public void setNickName(String nickName) {
                this.nickName = nickName;
        }

        public Boolean getNoDonate() {
                return noDonate;
        }

        public void setNoDonate(Boolean noDonate) {
                this.noDonate = noDonate;
        }

        public Long getUid() {
                return uid;
        }

        public void setUid(Long uid) {
                this.uid = uid;
        }

        public String getWebPay() {
                return webPay;
        }

        public void setWebPay(String webPay) {
                this.webPay = webPay;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getHomepage() {
                return homepage;
        }

        public void setHomepage(String homepage) {
                this.homepage = homepage;
        }

        public Short getSource() {
                return source;
        }

        public void setSource(Short source) {
                this.source = source;
        }

        public Short getState() {
                return state;
        }

        public void setState(Short state) {
                this.state = state;
        }

        public Long getSourceId() {
                return sourceId;
        }

        public void setSourceId(Long sourceId) {
                this.sourceId = sourceId;
        }
}
