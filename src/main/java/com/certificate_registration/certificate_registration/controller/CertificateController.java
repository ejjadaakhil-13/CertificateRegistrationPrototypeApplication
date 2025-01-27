package com.certificate_registration.certificate_registration.controller;

import com.certificate_registration.certificate_registration.model.Certificate;
import com.certificate_registration.certificate_registration.model.User;
import com.certificate_registration.certificate_registration.service.CertificateService;
import com.certificate_registration.certificate_registration.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private UserService userService;

    /**
     * Add the session attribute for the logged-in user.
     */
    @ModelAttribute("loggedInUser")
    public String getLoggedInUser(HttpSession session) {
        return (String) session.getAttribute("loggedInUser");
    }

    /**
     * Display the login page.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Handle login requests.
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if (userService.validateUser(username, password)) {
            session.setAttribute("loggedInUser", username);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    /**
     * Handle logout requests and invalidate the session.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    /**
     * Display the registration form if logged in.
     */
    @GetMapping("/")
    public String showRegistrationForm(@ModelAttribute("loggedInUser") String loggedInUser, Model model) {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("certificate", new Certificate());
        return "register";
    }

    /**
     * Handle the registration of a certificate.
     */
    @PostMapping("/register")
    public String registerCertificate(@ModelAttribute Certificate certificate,
                                      @RequestParam("file") MultipartFile file,
                                      @ModelAttribute("loggedInUser") String loggedInUser) throws IOException {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        certificateService.registerCertificate(certificate, file);
        return "redirect:/admin";
    }

    /**
     * Display the admin dashboard with optional search functionality.
     */
    @GetMapping("/admin")
    public String showAdminDashboard(@RequestParam(required = false) String search,
                                     @ModelAttribute("loggedInUser") String loggedInUser,
                                     Model model) {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("certificates", certificateService.getCertificates(search));
        return "admin";
    }

    /**
     * View the certificate file based on its ID.
     */
    @GetMapping("/certificate/{id}")
    public ResponseEntity<byte[]> viewCertificate(@PathVariable Long id,
                                                  @ModelAttribute("loggedInUser") String loggedInUser) {
        if (loggedInUser == null) {
            return ResponseEntity.status(403).build(); // Forbidden if not logged in
        }
        return certificateService.viewCertificate(id);
    }

    /**
     * Display the edit form for a specific certificate.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @ModelAttribute("loggedInUser") String loggedInUser,
                               Model model) {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Certificate certificate = certificateService.getCertificateById(id);
        model.addAttribute("certificate", certificate);
        return "edit";
    }

    /**
     * Handle updates to an existing certificate.
     */
    @PostMapping("/edit/{id}")
    public String updateCertificate(@PathVariable Long id,
                                    @ModelAttribute Certificate certificate,
                                    @RequestParam(required = false) MultipartFile file,
                                    @ModelAttribute("loggedInUser") String loggedInUser) throws IOException {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        certificateService.updateCertificate(id, certificate, file);
        return "redirect:/admin";
    }

    /**
     * Delete a certificate based on its ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteCertificate(@PathVariable Long id,
                                    @ModelAttribute("loggedInUser") String loggedInUser) {
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        certificateService.deleteCertificate(id);
        return "redirect:/admin";
    }
    
    /**
     * Optional: Method to handle 404 errors (e.g., when a certificate is not found).
     */
    @ExceptionHandler({IllegalArgumentException.class})
    public String handleException(IllegalArgumentException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error";
    }
}
