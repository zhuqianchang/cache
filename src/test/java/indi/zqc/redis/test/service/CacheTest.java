package indi.zqc.redis.test.service;

import com.codahale.metrics.Timer;
import indi.zqc.redis.monitor.CacheMetrics;
import indi.zqc.redis.service.CacheFacade;
import indi.zqc.redis.test.Application;
import indi.zqc.redis.test.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class CacheTest {

    @Resource
    private CacheFacade<CacheMetrics> cacheFacade;

    @Test
    public void test() throws InterruptedException {

        cacheFacade.delete("test_user", "test_str");

        User user = new User("zqc", "a1234567");
        cacheFacade.set("test_user", user);
        Assert.assertTrue(cacheFacade.exists("test_user"));

        User u = cacheFacade.get("test_user", User.class);
        Assert.assertNotNull(u);
        Assert.assertEquals(u.getUsername(), "zqc");
        Assert.assertEquals(u.getPassword(), "a1234567");

        cacheFacade.delete("test_user");
        Assert.assertFalse(cacheFacade.exists("test_user"));

        cacheFacade.set("test_str", "zqc");
        Assert.assertTrue(cacheFacade.exists("test_str"));
        Assert.assertEquals(cacheFacade.get("test_str"), "zqc");

        cacheFacade.expire("test_str", 3L);//3S
        Thread.sleep(1000);
        Assert.assertTrue(cacheFacade.exists("test_str"));
        Thread.sleep(3000);
        Assert.assertFalse(cacheFacade.exists("test_str"));

        cacheFacade.set("test_str", "zqc", 3L);//3S
        Thread.sleep(1000);
        Assert.assertTrue(cacheFacade.exists("test_str"));
        Thread.sleep(3000);
        Assert.assertFalse(cacheFacade.exists("test_str"));
    }

    @Test
    public void test2() {
        cacheFacade.set("test_user1", "test_user1");
        cacheFacade.set("test_user2", "test_user2");
        cacheFacade.set("test_user3", "test_user3");
        cacheFacade.set("test_user4", "test_user4");
        cacheFacade.set("test_user5", "test_user5");
        cacheFacade.set("test_user66", "test_user66");
        String[] keys = cacheFacade.keys("test_user*");
        Assert.assertEquals(keys.length, 6);
        for (String key : keys) {
            System.out.println(key + ":" + cacheFacade.get(key));
        }
        cacheFacade.delete("test_user1", "test_user2", "test_user3", "test_user4", "test_user5","test_user66");
        keys = cacheFacade.keys("test_user*");
        Assert.assertEquals(keys.length, 0);
    }

    @Test
    public void matches(){
        Assert.assertTrue(Pattern.matches("cache_user_test.*","cache_user_test1"));
        Assert.assertTrue(Pattern.matches("[a-z]{3}","abc"));
        Assert.assertTrue("cache_user_test1".matches("cache_user_test.*"));
    }

    @Test
    public void metrics() {
        CacheMetrics metrics = cacheFacade.getMetrics();
        Timer getOperationTimer = metrics.getGetOperationTimer();
        System.out.println(getOperationTimer.getCount());
    }

}
