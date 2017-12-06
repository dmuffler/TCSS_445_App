# TCSS_445_App

Members: 	Donald Muffler
			Steven Smith
			Brian Geving
			Dmitriy Bliznyuk
			
Group:		A

Files:		<API (JAVA)>								Description:
			API											API class for api calls.
			CreateInstructorTask						Creates an instuctor.
			DeleteInstructorTask						Deletes and instructor.
			DeleteStudentTask							Deletes a student.
			ReadStudentTask								Reads in a students info.
			SearchStudentTask							Searches for a student review.			
			UpdateStudentTask							Updates a student's review.
			
			<Back-End Fragments (JAVA)>					
			AddInstructorFragment						Adds an instructor.
			AdminMenuFragment							The menu for admins.
			AdminStudentManagementFragment				Admin deletes students.
			InstructorFragment							List of instructors.
			InstructorFragmentAdmin						List of instructors for an admin that can delete instructors.
			LoginFragment								Login handling.
			RegisterFragment							Register handling.
			SearchFragment								Search handling for students.
			SearchFragmentAdmin							Search handling for admins.
			StudentSettingsFragment						Change credentials for a student.
			
			<UI Fragments (XML)>
			activity_main								Main activity layout.
			app_bar_main								App bar layout.
			content_main								Main container for UI fragments.
			fragment_add_instructor						UI for adding instructors.
			fragment_admin_student_management			UI for admins managing students.
			fragment_login								Main page.
			fragment_register							Register page.
			fragment_search								Search instructors page.
			fragment_search_admin						Search student's page.
			fragment_student_settings					Student settings page.
			instructor_admin_fragment					UI for admins on the instructor page.
			instructor_layout							Instructor page.
			layout_instructor_list_item					Layout of the instructor list.
			layout_rating_list_item						Layout of the rating.
			layout_student_list_item					Layout of the student list.
			nav_header_main								Head of the app.
			
			<Misc Classes (JAVA)>						
			Instructor									Instructor parcing.
			InstructorListAdapter						List of instructors.
			InstructorResult							Instructor info.
			LoginResult									User info.
			MainActivity								Main activity that deals with fragment transactions.
			Rating										Ratings for specific instructors.
			RatingListAdapter							List of ratings.
			RatingResult								Rating info.
			Session										Session info.
			Student										Student info.
			StudentListAdapter							List of students.
			StudentResult								Student info.
			
			<PHP>										
			db											Database connection.
			instructor									Instructor handling.
			login										Login handling.
			rating										Rating handling.
			register									Register handling.
			session_test								Session testing file.
			student										Student handling.
			
			<SQL>										
			create										Creates the database.
			
			<TXT>										
			Faculty Scrape								Scrapes the UW faculty page to populate the Instructor table.