package inPoint;

import core.createDTO.ChildrenCreateDTO;
import core.viewDTO.ChildrenViewDTO;
import dao.ds.fabric.DataSourceSingleton;
import dao.fabric.AddressDaoSingleton;
import dao.fabric.ChildrenDaoSingleton;
import dao.fabric.ParentDaoSingleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.api.IChildrenService;
import service.fabric.AddressServiceSingleton;
import service.fabric.ChildrenServiceSingleton;
import service.fabric.ParentServiceSingleton;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ChildrenServlet", urlPatterns = "/children")
public class ChildrenServlet extends HttpServlet {
    private final IChildrenService service;
    private final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private final String CHARACTER_ENCODING = "UTF-8";

    public ChildrenServlet() throws PropertyVetoException {
        this.service = ChildrenServiceSingleton.getInstance(
                ChildrenDaoSingleton.getInstance(DataSourceSingleton.getInstance()),
                ParentServiceSingleton.getInstance(
                        ParentDaoSingleton.getInstance(DataSourceSingleton.getInstance()),
                        AddressServiceSingleton.getInstance(AddressDaoSingleton.getInstance(DataSourceSingleton.getInstance()))
                )
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        Writer writer = resp.getWriter();
        if(idStr==null||idStr.isBlank()||idStr.isEmpty()){
            List<ChildrenViewDTO> childrenViewDTOS = service.getAll();
            for(ChildrenViewDTO viewDTO : childrenViewDTOS){
                writer.write("<p>"+viewDTO.toString()+"</p>");
            }
        } else {
            long id = Long.valueOf(idStr);
            ChildrenViewDTO childrenView = service.get(id);
            writer.write(childrenView.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);


        ChildrenCreateDTO createDTO = childrenCreateDTO(req);
        service.create(createDTO);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        if(idStr==null||idStr.isEmpty()||idStr.isBlank())
            throw new IllegalArgumentException("Не введен id");
        String versionStr = req.getParameter("version");
        if(versionStr==null||versionStr.isEmpty()||versionStr.isBlank())
            throw new IllegalArgumentException("Не введена версия");
        ChildrenCreateDTO updateDTO = childrenCreateDTO(req);
        long id = Long.valueOf(idStr);
        long version = Long.valueOf(versionStr);
        service.update(id,version,updateDTO);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        String idStr = req.getParameter("id");
        if(idStr==null||idStr.isEmpty()||idStr.isBlank())
            throw new IllegalArgumentException("Не введен id");
        String versionStr = req.getParameter("version");
        if(versionStr==null||versionStr.isEmpty()||versionStr.isBlank())
            throw new IllegalArgumentException("Не введена версия");
    }

    private ChildrenCreateDTO childrenCreateDTO(HttpServletRequest req){
        String name = req.getParameter("name");
        if(name==null||name.isEmpty()||name.isBlank())
            throw new IllegalArgumentException("Не введено имя");
        String[] idParentSrt = req.getParameterValues("id_parent");
        if(idParentSrt==null||idParentSrt.length==0)
            throw new IllegalArgumentException("Не введен id родителя");
        List<Long> idParents = new ArrayList<>();
        for (String stringID : idParentSrt){
            if(stringID!=null||!stringID.isEmpty()||!stringID.isBlank()){
                idParents.add(Long.valueOf(stringID));
            }
        }
        if(idParents.size()==0)
            throw new IllegalArgumentException("Не введен id родителя");
        return new ChildrenCreateDTO(name,idParents);
    }
}
