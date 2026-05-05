package com.voluntrack.api.service;

import com.voluntrack.api.model.Attendance;
import com.voluntrack.api.repository.AttendanceRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
  private final AttendanceRepository attendance;

  public CertificateService(AttendanceRepository attendance) {
    this.attendance = attendance;
  }

  public byte[] generate(Long attendanceId) throws IOException {
    Attendance record = attendance.findById(attendanceId).orElseThrow();
    try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      PDPage page = new PDPage();
      document.addPage(page);
      try (PDPageContentStream content = new PDPageContentStream(document, page)) {
        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 28);
        content.newLineAtOffset(120, 650);
        content.showText("Certificate of Appreciation");
        content.endText();

        content.beginText();
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 16);
        content.newLineAtOffset(95, 560);
        content.showText("Presented to " + record.getVolunteer().getName());
        content.newLineAtOffset(0, -40);
        content.showText("For completing " + record.getHoursWorked() + " volunteer hour(s)");
        content.newLineAtOffset(0, -40);
        content.showText("Task: " + record.getTask().getTitle());
        content.newLineAtOffset(0, -40);
        content.showText("Event: " + record.getTask().getEvent().getTitle());
        content.endText();
      }
      document.save(out);
      return out.toByteArray();
    }
  }
}
