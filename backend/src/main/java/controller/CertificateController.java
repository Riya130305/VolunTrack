package com.voluntrack.api.controller;

import com.voluntrack.api.service.CertificateService;
import java.io.IOException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
  private final CertificateService certificates;

  public CertificateController(CertificateService certificates) {
    this.certificates = certificates;
  }

  @GetMapping("/{attendanceId}")
  public ResponseEntity<byte[]> certificate(@PathVariable Long attendanceId) throws IOException {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=voluntrack-certificate.pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(certificates.generate(attendanceId));
  }
}
