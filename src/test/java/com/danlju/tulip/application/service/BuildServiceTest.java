package com.danlju.tulip.application.service;

import com.danlju.tulip.core.domain.Build;
import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.core.domain.exceptions.IllegalBuildStateTransitionException;
import com.danlju.tulip.application.repository.BuildRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuildServiceTest {

    @Mock
    private BuildRepository buildRepository;

    @InjectMocks
    private BuildService buildService;

    @Test
    public void should_update_status_for_build() {

        when(buildRepository.findById(1)).thenReturn(
            new Build(UUID.randomUUID(), 1, 1001, BuildStatus.REQUESTED, "commit", "commitMessage")
        );

        buildService.updateStatusForBuild(1, "running");

        verify(buildRepository, times(1)).save(any());
    }

    @Test
    public void should_fail_on_illegal_state_transition() {
        when(buildRepository.findById(1)).thenReturn(
                new Build(UUID.randomUUID(), 1, 1001, BuildStatus.REQUESTED, "commit", "commitMessage")
        );

        assertThrows(IllegalBuildStateTransitionException.class, () -> {
            buildService.updateStatusForBuild(1, "completed");
        });
    }
}