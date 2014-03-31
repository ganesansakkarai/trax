package org.kits.trax.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonUtilTest {

    @Test
    public void toJson() {

        Group group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        String json = JsonUtil.toJson(group);
        Assert.assertNotNull(json);
    }

    @Test
    public void toJsonArray() {

        List<Group> groups = new ArrayList<Group>();
        Group group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        groups.add(group);
        group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        groups.add(group);
        String json = JsonUtil.toJsonArray(groups);
        Assert.assertNotNull(json);
    }

    @Test
    public void fromJson() {

        Group group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        String json = JsonUtil.toJson(group);
        Group proj = JsonUtil.fromJson(Group.class, json);
        Assert.assertNotNull(proj);
    }

    @Test
    public void fromJsonArray() {

        List<Group> groups = new ArrayList<Group>();
        Group group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        groups.add(group);
        group = new Group();
        group.setName("Sample");
        group.setDescription("A sample projecct");
        groups.add(group);
        String json = JsonUtil.toJsonArray(groups);
        List<Group> projs = JsonUtil.fromJsonArray(Group.class, json);
        Assert.assertNotNull(projs);
        Assert.assertTrue(projs.size() == 2);
    }
}
