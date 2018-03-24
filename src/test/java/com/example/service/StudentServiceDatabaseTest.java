package com.example.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.example.dao.StudentMapper;
import com.example.model.StudentModel;

public class StudentServiceDatabaseTest {

	private StudentService studentService = new StudentServiceDatabase();

	@Mock
	private StudentMapper studentMapper;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.studentService = new StudentServiceDatabase(this.studentMapper);
	}

	@Test
	public void selectStudent() {
		// given
		StudentModel studentModel = new StudentModel("1506737823", "Chanek", 3.5);
		StudentModel check = new StudentModel("1506737823", "Chanek", 3.5);
		BDDMockito.given(studentMapper.selectStudent("1506737823")).willReturn(studentModel);

		// when
		StudentModel test = studentService.selectStudent("1506737823");

		// then
		assertThat(test, notNullValue());
		assertThat(test, equalTo(check));
	}

	@Test
	public void selectAllStudents() {
		// given
		List<StudentModel> studentModels = new ArrayList<>();
		StudentModel studentModel = new StudentModel("1506737823", "Chanek", 3.5);
		studentModels.add(studentModel);

		List<StudentModel> checks = new ArrayList<>();
		StudentModel check = new StudentModel("1506737823", "Chanek", 3.5);
		checks.add(check);

		BDDMockito.given(studentMapper.selectAllStudents()).willReturn(studentModels);

		// when
		List<StudentModel> test = studentService.selectAllStudents();

		// then
		assertThat(test, notNullValue());
		assertThat(test.isEmpty(), equalTo(false));
		assertThat(test.size(), equalTo(1));
		assertThat(test, equalTo(checks));

	}

	@Test
	public void addStudent() {
		// given
		StudentModel studentModel = new StudentModel("1506737823", "Chanek", 3.5);
		StudentModel check = new StudentModel("1506737823", "Chanek", 3.5);
		BDDMockito.given(studentService.addStudent(studentModel)).willReturn(true);

		// when
		boolean test = studentService.addStudent(studentModel);

		// then
		BDDMockito.then(studentMapper).should().addStudent(check);
		assertThat(test, equalTo(true));
	}

	@Test
	public void deleteStudent() {
		// given
		List<StudentModel> students = new ArrayList<>();
		StudentModel student1 = new StudentModel("1506737823", "Chanek", 3.5);
		StudentModel student2 = new StudentModel("1506737824", "Chanek J", 3.5);
		students.add(student1);
		students.add(student2);
		//BDDMockito.given(studentMapper.deleteStudent("1506737823")).willReturn(true);  
		BDDMockito.given(studentMapper.deleteStudent(Mockito.anyString())).will(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				String npm = invocation.getArgumentAt(0, String.class);
				for (StudentModel studentModel : students) {
					if (studentModel.getNpm().equals(npm)) {
						students.remove(studentModel);
						return true;
					}
				}
				return false;
			}
			
		});
		
		//when
		boolean test = studentService.deleteStudent("1506737823");
		
		// then
		//BDDMockito.then(studentMapper).should().deleteStudent("1506737823");
		assertThat(test, equalTo(true));
		assertThat(students.size(), equalTo(1));
		assertThat(students.contains(student1), equalTo(false));
	}

	@Test
	public void updateStudent() {
		// given
		StudentModel student = new StudentModel("1506737823", "Chanek", 3.5);
		BDDMockito.given(studentService.updateStudent(student)).willReturn(true);

		// when
		boolean test = studentService.updateStudent(student);

		// then
		BDDMockito.then(studentMapper).should().updateStudent(student);
		assertThat(test, equalTo(true));
	}
}
