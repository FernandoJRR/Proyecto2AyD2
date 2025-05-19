package com.ayd.game_service.shared.config;

import com.ayd.game_service.holes.models.Hole;
import com.ayd.game_service.holes.repositories.HoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class SeedersConfigTest {

    @Mock
    private HoleRepository holeRepository;

    @InjectMocks
    private SeedersConfig seedersConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * dado: que no hay hoyos registrados en la base.
     * cuando: se ejecuta el método run().
     * entonces: se crean y guardan 18 hoyos.
     */
    @Test
    void runShouldSeedHolesWhenNoneExist() throws Exception {
        // Arrange
        when(holeRepository.count()).thenReturn(0L);

        // Act
        seedersConfig.run();

        // Assert
        verify(holeRepository, times(18)).save(Mockito.any(Hole.class));
    }

    /**
     * dado: que ya existen hoyos en la base.
     * cuando: se ejecuta el método run().
     * entonces: no se crean ni guardan nuevos hoyos.
     */
    @Test
    void runShouldNotSeedIfHolesExist() throws Exception {
        // Arrange
        when(holeRepository.count()).thenReturn(18L);

        // Act
        seedersConfig.run();

        // Assert
        verify(holeRepository, never()).save(any());
    }
}
