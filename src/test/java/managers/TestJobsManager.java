package managers;

import com.eonnations.eonjobs.EonJobs;
import com.eonnations.eonjobs.jobs.Job;
import com.eonnations.eonjobs.jobs.Jobs;
import com.eonnations.eonjobs.jobs.JobsManager;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestJobsManager {
    private EonJobs plugin;
    private JobsManager jobsManager;
    private UUID testUuid;
    private final Jedis jedis = new FakeJedis();
    private Job testJob;

    @BeforeEach
    public void setUp() {
        JedisPool pool = mock(JedisPool.class);
        when(pool.getResource()).thenReturn(jedis);
        plugin = mock(EonJobs.class);
        jobsManager = new JobsManager(pool, plugin);
        testUuid = UUID.randomUUID();
        testJob = new Job(Jobs.FARMER, 50);
    }

    @Test
    @DisplayName("Test getting a player's job, when it doesn't exist")
    public void testGetPlayerJob_notFound() {
        Job result = jobsManager.getPlayerJob(testUuid);
        assertEquals(Jobs.MINER, result.getEnumJob());
        assertEquals(0.0, result.getExp());
    }

    @Test
    @DisplayName("Test adding a job for a player")
    public void testAddPlayerToJob() {
        jobsManager.addPlayerToJob(testUuid, testJob.getEnumJob(), testJob.getExp());
        Job result = jobsManager.getPlayerJob(testUuid);
        assertEquals(Jobs.FARMER, result.getEnumJob());
        assertEquals(50.0, result.getExp());
    }

    @Test
    @DisplayName("Test checking if a player has a job, when they do")
    public void testHasJob() {
        jobsManager.addPlayerToJob(testUuid, testJob.getEnumJob(), testJob.getExp());
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(testUuid);
        assertTrue(jobsManager.hasJob(player));
    }

    @Test
    @DisplayName("Test checking if a player has a job, when they don't")
    public void testHasJob_notFound() {
        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(testUuid);
        assertFalse(jobsManager.hasJob(player));
    }
}
