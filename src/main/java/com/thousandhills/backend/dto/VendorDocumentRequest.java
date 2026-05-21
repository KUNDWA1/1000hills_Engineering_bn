package com.thousandhills.backend.dto;

public class VendorDocumentRequest {

    private String businessLicenseUrl;
    private String nationalIdUrl;
    private String companyCertificateUrl;
    private String companyName;
    private String companyAddress;

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl;
    }

    public String getNationalIdUrl() {
        return nationalIdUrl;
    }

    public void setNationalIdUrl(String nationalIdUrl) {
        this.nationalIdUrl = nationalIdUrl;
    }

    public String getCompanyCertificateUrl() {
        return companyCertificateUrl;
    }

    public void setCompanyCertificateUrl(String companyCertificateUrl) {
        this.companyCertificateUrl = companyCertificateUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
}
