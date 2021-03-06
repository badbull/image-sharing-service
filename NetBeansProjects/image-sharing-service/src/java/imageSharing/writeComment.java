/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageSharing;

import imageSharingDatabase.Comments;
import imageSharingDatabase.Images;
import imageSharingDatabase.Users;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Artsi
 */
@WebServlet(name = "writeComment", urlPatterns = {"/writeComment/"})
public class writeComment extends HttpServlet {
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
        PrintWriter out = response.getWriter();
        try {
                emf = Persistence.createEntityManagerFactory("image-sharing-servicePU");
                em = emf.createEntityManager();
                
                
                String commentToWrite = request.getParameter("comment");
                String imageId = request.getParameter("id");
                
                int userId = Integer.parseInt(request.getParameter("userId"));
                
                int fkImg = Integer.parseInt(imageId);
                
                Query q = em.createQuery("SELECT u FROM Users u");
                List<Users> userList = q.getResultList();
                Users uploadUser = new Users();
                for (Users user : userList) {
                    if(user.getId().equals(userId)){
                         uploadUser = user;
                    }
                }
                                
                em.getTransaction().begin();
                
                Comments comment = new Comments();
                comment.setText(commentToWrite);
                Date date = new Date();
                
                comment.setTimeStamp(new Timestamp(date.getTime()));
                comment.setFKwriter(uploadUser);
                
                for (Images c : (List<Images>) em.createQuery("SELECT c FROM Images c").getResultList()) {
                    if (c.getId() == fkImg){
                        comment.setFKimg(c);
                    }
                }
                
                em.persist(comment);
               
                em.getTransaction().commit();
                
                
            } catch (Exception e) {
                out.println(e);
            } finally {
                em.close();
                emf.close();
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
