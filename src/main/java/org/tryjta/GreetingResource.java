package org.tryjta;

import io.quarkus.narayana.jta.runtime.interceptor.TransactionalInterceptorRequired;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@Path("/my-entities")
@Slf4j
public class GreetingResource {

    @Inject
    EntityManager em;
    @Inject
    XACreateFileService createFileService;
    @Inject
    XADummyService dummyService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public List<MyEntity> list() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MyEntity> cr = cb.createQuery(MyEntity.class);
        Root<MyEntity> root = cr.from(MyEntity.class);
        cr.select(root);

        TypedQuery<MyEntity> query = em.createQuery(cr);
        List<MyEntity> results = query.getResultList();
        return results;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public MyEntity create(@QueryParam("field") String field) throws SystemException, RollbackException {
        MyEntity entity =  MyEntity.builder().field(field).build();
        createFileService.createFile(String.format("/home/arch/test-jta/%s-%s", field, UUID.randomUUID()));
        dummyService.writeOnConsole(field);
        em.persist(entity);
        return entity;
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("no-transaction")
    public MyEntity noTransaction(@QueryParam("field") String field) throws SystemException, RollbackException {
        MyEntity entity =  MyEntity.builder().field(field).build();
        dummyService.writeOnConsole(field);
        createFileService.createFile(String.format("/home/arch/test-jta/%s-%s", field, UUID.randomUUID()));
        em.persist(entity);
        return entity;
    }
}
