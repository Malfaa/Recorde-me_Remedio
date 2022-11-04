package com.malfaa.recorde_me_remedio.remedio.adicionar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.malfaa.recorde_me_remedio.local.Remedio
import com.malfaa.recorde_me_remedio.repository.FakeRepositoryTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AdicionarViewModelTest {

    val dispatcher = StandardTestDispatcher()
    val scope = TestScope(dispatcher)

    private lateinit var adicionarViewModel: AdicionarViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var adicionarRepository: FakeRepositoryTest

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){
        Dispatchers.setMain(dispatcher)

        adicionarRepository = FakeRepositoryTest()

//        adicionarViewModel = AdicionarViewModel(adicionarRepository)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    @Test
    fun setRemedio_VerificaComportamento() = scope.runTest{
        val remedio = Remedio(1, "um",2,4,5L,null,false,1)

        adicionarViewModel.adicionarRemedio(remedio)

        MatcherAssert.assertThat(adicionarRepository.getRecebeItem(), CoreMatchers.`is`(true))
    }

    
}