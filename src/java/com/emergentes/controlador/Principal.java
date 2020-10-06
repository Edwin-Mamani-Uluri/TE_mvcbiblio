package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.modelo.LibroDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Principal", urlPatterns = {"/Principal"})
public class Principal extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Principal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Principal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession ses = request.getSession();
        //todos los libros almacenan en el atributo gestor
        LibroDAO gestor = (LibroDAO) ses.getAttribute("gestor");
        //verificamos si el atributo exite
        if (gestor == null) {
            //en caso de que no exista se incluye el atributo
            LibroDAO axi = new LibroDAO();
            ses.setAttribute("gestor", axi);
        }
        String op = request.getParameter("op");
        if (op == null) {
            op = "";
        }
        //el controlador verifica la opcion para direccionar al recurso correntpondiente
        if (op.trim().equals("")) {
            response.sendRedirect("vista/listado.jsp");

        }
        if (op.trim().equals("nuevo")) {
            Libro obj = new Libro();
            request.setAttribute("item", obj);
            request.getRequestDispatcher("vista/nuevo.jsp").forward(request, response);
        }
        if (op.trim().equals("editar")) {
            int pos = gestor.posicion(Integer.parseInt(request.getParameter("id")));
            Libro obj = gestor.getLibros().get(pos);

            request.setAttribute("item", obj);
            request.getRequestDispatcher("vista/editar.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession ses = request.getSession();
        //todos los libros almacenan en el atributo gestor
        LibroDAO gestor = (LibroDAO) ses.getAttribute("gestor");
        Libro obj=new Libro();
        obj.setId(Integer.parseInt(request.getParameter("id")));
        obj.setAutor(request.getParameter("autor"));
        obj.setTitulo(request.getParameter("titulo"));
        obj.setEstado(Integer.parseInt(request.getParameter("estado")));
        
        String tipo=request.getParameter("tipo");
        
        if (tipo.equals("-1")) {
            gestor.insetar(obj);
        }else{
        gestor.modificar(obj.getId(), obj);
        }
        response.sendRedirect("vista/listado.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
