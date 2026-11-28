package sonidoVivo.tienda.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Entidad Usuario: se guarda en la tabla "usuario"
// Sirve para iniciar sesión (username, password, rol) y para el perfil de la tienda
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // Aquí se guarda la contraseña ya encriptada (BCrypt), nunca en texto plano
    @Column(nullable = false)
    private String password;

    // Rol del usuario: "USER" o "ADMIN"
    @Column(nullable = false)
    private String rol;

    // Datos del perfil (los mismos que pide el registro del frontend)
    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String rut;       // formato 12345678-9, sin puntos

    @Column(unique = true)
    private String email;

    private LocalDate fechaNacimiento;
    private String instrumento;
    private String nivel;

    public Usuario() {
    }

    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getInstrumento() { return instrumento; }
    public void setInstrumento(String instrumento) { this.instrumento = instrumento; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
}