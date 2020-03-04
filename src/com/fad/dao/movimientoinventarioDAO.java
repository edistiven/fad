package com.fad.dao;

import com.fad.controller.ExistenciaJpaController;
import com.fad.controller.MovimientoinventarioJpaController;
import com.fad.controller.OrdeninventarioJpaController;
import com.fad.controller.TipordeninvJpaController;
import com.fad.entities.Categoria;
import com.fad.entities.Existencia;
import com.fad.entities.Movimientoinventario;
import com.fad.entities.Ordeninventario;
import com.fad.entities.Tipordeninv;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author GabuAndLemo
 */
public class movimientoinventarioDAO {

    /**
     * Servicios *
     */
    private MovimientoinventarioJpaController mjc = new MovimientoinventarioJpaController();
    private OrdeninventarioJpaController ojc = new OrdeninventarioJpaController();
    private ExistenciaJpaController ejc = new ExistenciaJpaController();

    /**
     * Variables *
     */
    /* Orden de Inventario */
    private static Ordeninventario ordenInventario = new Ordeninventario();
    private static List<Ordeninventario> ordenInventarios = new ArrayList<>();

    /* Mov de Inventario */
    private static Movimientoinventario movimientoInventario = new Movimientoinventario();
    private static List<Movimientoinventario> movimientoInventariosTemp = new ArrayList<>();

    /* Existencias */
    private static Existencia existencia = new Existencia();
    private static List<Existencia> existencias = new ArrayList<>();
    private static List<Existencia> existenciasTemp = new ArrayList<>();

    /* Tipo de Inventario */
    private static Tipordeninv tipordeninv = new Tipordeninv();

    /* Categoria */
    private static Categoria categoria = new Categoria();

    /* responsable */
    /**
     * Funciones basicas CRUD*
     */
    public void insertar() {
        try {
            // Setteo de Id Orden
            ordenInventario.setIdOrdeninventario(obtenerId(categoria));

            //Setteo de Id en MOV
            cambioIdMov();

            //Modificacion de las existencias
            //Guardar Orden Inventario
            ojc.create(ordenInventario);

            //Guardar Mov Inventario
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error!!!: ");
            e.printStackTrace();
        }
    }

    public void modificar(int ids, String nombreToi, String descripcionToi) {
        try {

            //tipordeninv = new Tipordeninv();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error!!!" + e);
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        try {

        } catch (Exception e) {

        }
    }

    public void listarOrdenesInventarios(JTable tablaOI, String id) {
        DefaultTableModel model;
        String[] titulosU = {"Id", "Responsable", "Fecha", "Descripcion"};
        model = new DefaultTableModel(null, titulosU);
        ordenInventarios = buscarOrden(id);

        String[] datosO = new String[5];

        for (Ordeninventario o : ordenInventarios) {
            datosO[0] = o.getIdOrdeninventario();
            datosO[1] = o.getIdTipordeninv().getNombreToi();
            datosO[2] = o.getResponsableOi();
            datosO[3] = cambioFecha(o.getFechaOi());
            datosO[4] = o.getDescripcionOi();

            model.addRow(datosO);
        }
        tablaOI.setModel(model);
    }

    public void insertarMov() {

        for (Movimientoinventario m : movimientoInventariosTemp) {
            try {
                m.setIdMovimiento(Integer.BYTES + "");
                m.setFechaMov(new Date());
                ejc.edit(m.getIdExistencia());
                mjc.create(m);
            } catch (Exception ex) {
                Logger.getLogger(movimientoinventarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addMovimientosInv() {

        movimientoInventariosTemp.add(movimientoInventario);
    }

    public void getTipoOICmb(JComboBox<Tipordeninv> cmbTipoOI) {
        EntityManager em = mjc.getEntityManager(); //
        Iterator it = em.createQuery("SELECT t FROM Tipordeninv t").getResultList().iterator();
        Tipordeninv tipordeninv;
        try {
            while (it.hasNext()) {
                tipordeninv = (Tipordeninv) it.next();
                cmbTipoOI.addItem(tipordeninv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No pudo cargar el combo");
        }

    }

    public void listarExistenciasByCategoria(JTable tablaE, String producto) {
        DefaultTableModel model;
        String[] titulosU = {"Id", "Producto", "Descripcion", "Categoría", "Existencia Inicial", "Existencia Actual", "V/U", "V/T"};
        model = new DefaultTableModel(null, titulosU);
        //List<Existencia> existencias = buscarExistenciasByCategoria(producto);
        
        System.out.println("esta es la lista a llenar: " + getExistencias().size());

        String[] datosE = new String[8];

        for (Existencia e : existencias) {
            datosE[0] = e.getIdExistencia();
            datosE[1] = e.getIdProducto().getNombrePro();
            datosE[2] = e.getIdProducto().getDescripcionPro();
            datosE[3] = e.getIdCategoria().getNombreCat();
            datosE[4] = e.getExistenciaIniE().toString();
            datosE[5] = e.getExistenciaActualE().toString();
            datosE[6] = e.getIdProducto().getValorUnitPro().toString();
            datosE[7] = e.getValorTotalE().toString();

            model.addRow(datosE);
        }
        tablaE.setModel(model);
    }

    public void getRolCmb(JComboBox<Categoria> cmbCategoria) {
        EntityManager em = ojc.getEntityManager(); //
        Iterator it = em.createQuery("SELECT c FROM Categoria c").getResultList().iterator();
        Categoria categoria;
        try {
            while (it.hasNext()) {
                categoria = (Categoria) it.next();
                cmbCategoria.addItem(categoria);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("No pudo cargar el combo");
        }

    }

    public String obtenerId(Categoria c) {

        String codigoAux = maxIdOrdenInv(c.getSiglaCat());
        String estructura;

        if (codigoAux == null) {
            if (c.getIdCategoria() < 10) {
                codigoAux = ("FAD." + c.getSiglaCat() + ".OI" + ".0" + c.getIdCategoria() + ".000");
            } else {
                codigoAux = ("FAD." + c.getSiglaCat() + ".OI" + "." + c.getIdCategoria() + ".000");
            }
        } else {
            if (c.getIdCategoria() < 10) {
                estructura = ("FAD." + c.getSiglaCat() + ".OI" + ".0" + c.getIdCategoria() + ".");
            } else {
                estructura = ("FAD." + c.getSiglaCat() + ".OI" + "." + c.getIdCategoria() + ".");
            }

            System.out.println("Este es el id que trae: " + codigoAux);

            String separador = Pattern.quote(".");
            String[] partsId = codigoAux.split(separador);
            System.out.println("Este es el id que trae: " + partsId);
            String codigoCortado = partsId[4];

            System.out.println("Este es el id convertido en numero: " + codigoCortado);

            Integer codigo = Integer.parseInt(codigoCortado);
            codigo = codigo + 1;

            System.out.println("Este es el id oficial: " + codigo);

            String codigoEx = codigo.toString();

            switch (codigoEx.length()) {
                case 1:
                    codigoAux = (estructura + "00" + codigoEx);
                    break;
                case 2:
                    codigoAux = (estructura + "0" + codigoEx);
                    break;
                case 3:
                    codigoAux = (estructura + codigoEx);
                    break;

                default:
                    break;
            }

            System.out.println("Este es el id: " + codigoAux);

        }

        return codigoAux;
    }

    public void cambioIdMov() {
        int i = 0;
        for (Movimientoinventario m : movimientoInventariosTemp) {
            m.setIdOrdeninventario(getOrdenInventario());
            movimientoInventariosTemp.set(i, m);
            i++;
        }
    }

    public String cambioFecha(Date fecha) {
        if (fecha == null) {
            return "No registrada";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String fechaFinal = format.format(fecha);

            return fechaFinal;
        }

    }

    /**
     * Consultas SQL*
     */
    private List<Ordeninventario> buscarOrden(String id) {

        EntityManager em = mjc.getEntityManager(); // SELECT o FROM Ordeninventario o WHERE o.idOrdeninventario = :idOrdeninventario
        Query sql = em.createQuery("SELECT o FROM Ordeninventario o WHERE o.idOrdeninventario LIKE :id ORDER BY o.idOrdeninventario");
        sql.setParameter("id", id + "%");
        List<Ordeninventario> lista = sql.getResultList();

        return lista;

    }

    public List<Existencia> buscarExistenciasByCategoria(String producto) {

        EntityManager em = ejc.getEntityManager(); //
        Query sql = em.createQuery("SELECT e FROM Existencia e WHERE e.idProducto.nombrePro LIKE :pro AND e.idCategoria.idCategoria = :id ORDER BY e.idProducto.nombrePro");
        sql.setParameter("pro", producto + "%");
        sql.setParameter("id", getCategoria().getIdCategoria());
        List<Existencia> lista = sql.getResultList();
        System.out.println("Este es la categoria: " + categoria.getNombreCat());
        return lista;

    }

    public String maxIdOrdenInv(String id) {

        EntityManager em = mjc.getEntityManager();

        StringBuilder queryString = new StringBuilder(
                "SELECT max(o.idOrdeninventario) FROM Ordeninventario o where o.idOrdeninventario like '%" + id + "%'"
        );

        Query query = em.createQuery(queryString.toString());

        try {
            String idExistencia = (String) query.getSingleResult();
            return idExistencia;
        } catch (NoResultException nre) {
            return null;
        } catch (NonUniqueResultException nure) {
            return null;
        }
    }

    /**
     * GET AND SET*
     */
    public static Ordeninventario getOrdenInventario() {
        return ordenInventario;
    }

    public static void setOrdenInventario(Ordeninventario ordenInventario) {
        movimientoinventarioDAO.ordenInventario = ordenInventario;
    }

    public static List<Ordeninventario> getOrdenInventarios() {
        return ordenInventarios;
    }

    public static void setOrdenInventarios(List<Ordeninventario> ordenInventarios) {
        movimientoinventarioDAO.ordenInventarios = ordenInventarios;
    }

    public static Movimientoinventario getMovimientoInventario() {
        return movimientoInventario;
    }

    public static void setMovimientoInventario(Movimientoinventario movimientoInventario) {
        movimientoinventarioDAO.movimientoInventario = movimientoInventario;
    }

    public static List<Movimientoinventario> getMovimientoInventariosTemp() {
        return movimientoInventariosTemp;
    }

    public static void setMovimientoInventariosTemp(List<Movimientoinventario> movimientoInventariosTemp) {
        movimientoinventarioDAO.movimientoInventariosTemp = movimientoInventariosTemp;
    }

    public static Existencia getExistencia() {
        return existencia;
    }

    public static void setExistencia(Existencia existencia) {
        movimientoinventarioDAO.existencia = existencia;
    }

    public static List<Existencia> getExistencias() {
        return existencias;
    }

    public static void setExistencias(List<Existencia> existencias) {
        movimientoinventarioDAO.existencias = existencias;
    }

    public static Tipordeninv getTipordeninv() {
        return tipordeninv;
    }

    public static void setTipordeninv(Tipordeninv tipordeninv) {
        movimientoinventarioDAO.tipordeninv = tipordeninv;
    }

    public static Categoria getCategoria() {
        return categoria;
    }

    public static void setCategoria(Categoria categoria) {
        movimientoinventarioDAO.categoria = categoria;
    }

}
