/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageSharing;

import imageSharingDatabase.Comments;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Artsi
 */
@WebServlet(name = "showComment", urlPatterns = {"/comment/*"})
public class showComment extends HttpServlet {

    EntityManagerFactory emf;
    EntityManager em;

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
        response.setContentType("application/json;charset=UTF-8");
        String imageIdRaw = request.getPathInfo().substring(1);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            try {

                emf = Persistence.createEntityManagerFactory("image-sharing-servicePU");
                em = emf.createEntityManager();

                JsonArrayBuilder builder = Json.createArrayBuilder();

                int fkImg = Integer.parseInt(imageIdRaw);
                Date date = new Date();
                DateFormat df = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
                
//                String action = request.getParameter("action");
//                
//                if("delete".equals(action)){
//                    Comments comment = em.find(Comments.class, imageId);
// 
//                    em.getTransaction().begin();
//                    em.remove(comment);
//                    em.getTransaction().commit();
//                }

                for (Comments c : (List<Comments>) em.createQuery("SELECT c FROM Comments c").getResultList()) {

                    if (fkImg == c.getFKimg().getId()) {
                        date = c.getTimeStamp();
                        String reportDate = df.format(date);
                        builder.add(Json.createObjectBuilder()
                                .add("comment", c.getText())
                                .add("date", reportDate)
                                .add("userName", c.getFKwriter().getUsername())
                                .add("id", c.getId()));
                    } else {
                        
                    }
                    
                }
                
                JsonArray arr = builder.build();
                out.println(arr);

            } catch (Exception e) {
                out.println(e);
            } finally {
                em.close();
                emf.close();
                out.close();
            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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
