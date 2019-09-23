package com.divroll.cms.client.services;

import com.divroll.backend.sdk.Divroll;
import com.divroll.backend.sdk.DivrollACL;
import com.divroll.backend.sdk.DivrollEntities;
import com.divroll.backend.sdk.DivrollEntity;
import com.divroll.backend.sdk.helper.Pair;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.Schema;
import com.divroll.http.client.exceptions.BadRequestException;
import io.reactivex.Single;
import org.jboss.errai.common.client.logging.util.Console;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class ContentService extends BaseService {

    @Inject
    SchemaService schemaService;

    public Single<Content> createContent(final Content content) {
            if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
                return Single.error(new IllegalArgumentException("No authentication token was found"));
            }
            DivrollACL acl = new DivrollACL();
            acl.setPublicRead(false);
            acl.setPublicWrite(false);
            acl.setAclRead(Arrays.asList("0-1", getLoggedInUser().getEntityId()));
            acl.setAclWrite(Arrays.asList("0-1", getLoggedInUser().getEntityId()));
            content.setAcl(acl);
            return content.create().flatMap(divrollEntity -> {
                content.setEntityId(divrollEntity.getEntityId());
                return Single.just(content);
            }).flatMap(content1 -> content1.setLink("schema", content1.getSchema().getEntityId())
                    .flatMap(isLinked -> Single.just(new Pair(content1, isLinked)))).flatMap(pair -> {
                Content content1 = (Content) pair.first;
                Boolean isLinked = (Boolean) pair.second;
                if(isLinked) {
                    return content1.setLink("user", getLoggedInUser().getEntityId())
                            .flatMap(isLinkedToUser -> Single.just(content1));
                } else {
                    return Single.error(new Exception("Error linking to user " + getLoggedInUser().getEntityId()));
                }
            });
    }

    public Single<Content> getContent(String contentId, String type) {
        DivrollEntity contentEntity = new DivrollEntity(type);
        contentEntity.setEntityId(contentId);
        return contentEntity.retrieve().flatMap(divrollEntity -> {
            Content content = copy(divrollEntity);
            return Single.just(content);
        });
    }

    /*
    public void subscribeContent(final String appId, final String className,
                               final AsyncCallback<Content> callback) {

        schemaService.listSchema(appId, new AsyncCallback<List<Schema>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }
            @Override
            public void onSuccess(List<Schema> schemas) {
                for(Schema schema : schemas) {
                    if(schema.getName().equals(className)) {
                        Console.log("Subscribing to liveQuery");
                        Where where = new Where("user", getLoggedInUser())
                                .where("schema", createPointer(SCHEMA_CLASSNAME, schema.getObjectId()));
                        ParseObject order = new ParseObject(CONTENT_CLASSNAME);
                        Parse.Query query = new Parse.Query(order);
                        query.where(where);
                        query.subscribe().on("create", new AsyncCallback<ParseObject>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }
                            @Override
                            public void onSuccess(ParseObject parseObject) {
                                try {
                                    Content content = (Content) parseObject;
                                    content.setContentType(className);
                                    callback.onSuccess(content);
                                } catch (Exception e) {
                                    callback.onFailure(e);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    */

    public Single<List<Content>> listContents(String entityType) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        DivrollEntities contentEntities = new DivrollEntities(entityType);
        return schemaService.listSchema().flatMap(schemas -> {
            Map<String, Schema> schemaMap = new LinkedHashMap<>();
            schemas.forEach(schema -> {
                if(schema != null) {
                    schemaMap.put(schema.getName(), schema);
                }
            });
            return contentEntities.query().flatMap( divrollEntities -> Single.just(new Pair(schemaMap, divrollEntities)) );
        }).flatMap(pair -> {
            DivrollEntities divrollEntities = (DivrollEntities) pair.second;
            Map<String, Schema> schemaMap = (Map<String, Schema>) pair.first;
            List<Content> contents = new LinkedList<>();
            divrollEntities.getEntities().forEach(divrollEntity -> {
                Content content = copy(divrollEntity);
                content.setSchema(schemaMap.get(content.getEntityType()));
                contents.add(content);
            });
            return Single.just(contents);
        });
    }

    public Single<Content> updateContent(final Content content){
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        return content.update().flatMap(updated -> {
            if(updated) {
                return Single.just(content);
            }
            return Single.error(new BadRequestException());
        });
    }

    public Single<Boolean> removeContent(String contentId, String type) {
        DivrollEntity divrollEntity = new DivrollEntity(type);
        divrollEntity.setEntityId(contentId);
        return divrollEntity.delete();
    }

    public Single<Boolean> publishContent(String contentId, String type) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        DivrollEntity divrollEntity = new DivrollEntity(type);
        divrollEntity.setEntityId(contentId);

        DivrollACL acl = new DivrollACL();
        acl.setPublicWrite(false);
        acl.setPublicRead(true);
        acl.setAclRead(Arrays.asList(getLoggedInUser().getEntityId()));
        acl.setAclWrite(Arrays.asList(getLoggedInUser().getEntityId()));

        divrollEntity.setAcl(acl);

        return divrollEntity.update();
    }

    public Single<Boolean> unpublishContent(String contentId, String type) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        DivrollEntity divrollEntity = new DivrollEntity(type);
        divrollEntity.setEntityId(contentId);

        DivrollACL acl = new DivrollACL();
        acl.setPublicWrite(false);
        acl.setPublicRead(false);
        acl.setAclRead(Arrays.asList(getLoggedInUser().getEntityId()));
        acl.setAclWrite(Arrays.asList(getLoggedInUser().getEntityId()));

        divrollEntity.setAcl(acl);

        return divrollEntity.update();
    }

}
