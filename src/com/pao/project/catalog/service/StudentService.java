package com.pao.project.catalog.service;
import com.pao.project.catalog.exception.EntitateNegasitaException;
import com.pao.project.catalog.exception.ValidareException;
import com.pao.project.catalog.model.Student;
import com.pao.project.catalog.repository.StudentRepository;
import java.util.*;
public class StudentService {
    private static StudentService instance;
    private final StudentRepository studentRepository;
    private final AuditService auditService;
    private StudentService() {
        this.studentRepository = new StudentRepository();
        this.auditService = AuditService.getInstance();
    }
    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }
    public void adaugaStudent(Student student) throws ValidareException {
        auditService.logAction("adauga_student");
        if (student == null || student.getNumarMatricol() == null) {
            throw new ValidareException("Studentul sau numărul matricol nu pot fi null!");
        }
        studentRepository.save(student);
    }
    public Student cautaStudent(String numarMatricol) throws EntitateNegasitaException {
        auditService.logAction("cauta_student");
        return studentRepository.findById(numarMatricol)
            .orElseThrow(() -> new EntitateNegasitaException("Studentul cu nr. matricol " + numarMatricol + " nu există!"));
    }
    public void listeazaTotiStudentii() {
        auditService.logAction("listeaza_studenti");
        System.out.println("--- Lista Studenți (Din BD) ---");
        List<Student> studenti = studentRepository.findAll();
        studenti.sort(Student::compareTo);
        for (Student s : studenti) {
            System.out.println(s);
        }
    }
    public void stergeStudent(String numarMatricol) throws EntitateNegasitaException {
        auditService.logAction("sterge_student");
        cautaStudent(numarMatricol); 
        studentRepository.delete(numarMatricol);
    }
}
