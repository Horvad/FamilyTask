package inPoint;

import core.FamilyAddressDTO;
import core.FamilyDTOChildren;
import core.FamilyDTOParent;
import core.viewDTO.ChildrenViewDTO;
import dao.FamilyDao;
import dao.ParentDao;
import dao.ds.fabric.DataSourceSingleton;
import dao.fabric.AddressDaoSingleton;
import dao.fabric.ChildrenDaoSingleton;
import dao.fabric.FamilyDaoSingleton;
import dao.fabric.ParentDaoSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ChildrenService;
import service.FamilyService;
import service.api.IChildrenService;
import service.api.IFamilyService;
import service.fabric.AddressServiceSingleton;
import service.fabric.ChildrenServiceSingleton;
import service.fabric.FamilyServiceSingleton;
import service.fabric.ParentServiceSingleton;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.Writer;

/**
 * получение полной информации по id ребенка, родителя или адресу
 */
@WebServlet(name = "FamilyServlet",urlPatterns = "/family")
public class FamilyServlet extends HttpServlet {
    private final IFamilyService service;
    private final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private final String CHARACTER_ENCODING = "UTF-8";

    public FamilyServlet() throws PropertyVetoException {
        this.service = FamilyServiceSingleton.getInstance(
                FamilyDaoSingleton.getInstance(DataSourceSingleton.getInstance()),
                AddressServiceSingleton.getInstance(
                        AddressDaoSingleton.getInstance(
                                DataSourceSingleton.getInstance())),
                ChildrenServiceSingleton.getInstance(
                        ChildrenDaoSingleton.getInstance(
                                DataSourceSingleton.getInstance()),
                        ParentServiceSingleton.getInstance(
                                ParentDaoSingleton.getInstance(
                                        DataSourceSingleton.getInstance()),
                                AddressServiceSingleton.getInstance(
                                        AddressDaoSingleton.getInstance(
                                                DataSourceSingleton.getInstance())))),
                ParentServiceSingleton.getInstance(
                        ParentDaoSingleton.getInstance(
                                DataSourceSingleton.getInstance()),
                        AddressServiceSingleton.getInstance(
                                AddressDaoSingleton.getInstance(
                                        DataSourceSingleton.getInstance())))
        );
    }

    /**
     * получение полной информации об родителе, ребенку, адересе
     * param : id, choice
     *
     * @return choice = address, return {@link core.FamilyAddressDTO}
     *         choice = children, return {@link core.FamilyDTOChildren}
     *         choice = parent, return {@link core.FamilyDTOParent}
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     *
     * @param resp an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String choice = req.getParameter("choice");
        String idStr = req.getParameter("id");

        Writer writer = resp.getWriter();
        if (idStr == null || idStr.isBlank() || idStr.isEmpty() ||
                choice == null || choice.isBlank() || choice.isEmpty()) {
            throw new IllegalArgumentException("Не введен id или не сделан выбор");
        }
        if(choice.equals("parent")){
            FamilyDTOParent parent = service.getFromParent(Long.valueOf(idStr));
            writer.write(parent.toString());
        }
        if(choice.equals("children")){
            FamilyDTOChildren children = service.getFromChildren(Long.valueOf(idStr));
            writer.write(children.toString());
        }
        if(choice.equals("address")){
            FamilyAddressDTO addressDTO = service.getFromAddress(Long.valueOf(idStr));
            writer.write(addressDTO.toString());
        }
    }
}