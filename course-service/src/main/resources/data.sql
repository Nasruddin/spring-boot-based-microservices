INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a',
    'Complete Python Bootcamp: Go from zero to hero in Python 3',
    'Learn Python like a Professional! Start from the basics and go all the way to creating your own applications and games!',
    60, 'EXTERNAL', 30, 5, false, true, false
    );

INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78b',
    'Java Programming Masterclass for Software Developers',
    'Learn Java In This Course And Become a Computer Programmer. Obtain valuable Core Java Skills And Java Certification',
    30, 'EXTERNAL', 50, 4, false, true, false
    );

INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78c',
    'Docker Mastery: The Complete Toolset From a Docker Captain',
    'Build, compose, deploy, and manage Docker containers from development to DevOps based Swarm clusters',
    15, 'EXTERNAL', 40, 3, false, true, false
    );


INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78d',
    'The Complete Digital Marketing Course - 12 Courses',
    'Master Digital Marketing: Strategy, Social Media Marketing, SEO, YouTube, Email, Facebook Marketing, Analytics & More!',
    5, 'INTERNAL', 100, 80, false, false, true
    );


INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78e',
    'Complete Guide to TensorFlow for Deep Learning with Python ',
    'Learn how to use Googles Deep Learning Framework - TensorFlow with Python! Solve problems with cutting edge techniques!',
    30, 'INTERNAL', 50, 22, false, false, false
    );


INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78f',
    'Angular & NodeJS - The MEAN Stack Guide',
    'Learn how to connect your Angular Frontend to a NodeJS & Express & MongoDB Backend by building a real Application',
    30, 'EXTERNAL', 50, 12, false, false, false
    );



INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78g',
    'Understanding TypeScript',
    'Donot limit the Usage of TypeScript to Angular 2! Learn the Basics, its Features, Workflows and how to use it!',
    30, 'EXTERNAL', 30, 12, false, false, false
    );




INSERT INTO courses (course_id,  course_name, course_desc, duration, type, total_seats, available_seats, expired, trending, featured)
VALUES ('f3831f8c-c338-4ebe-a82a-e2fc1d1ff78h',
    'Learn How To Code: Googles Go (golang) Programming Language',
    'The Ultimate Comprehensive Course Perfect for Both Beginners and Experienced Developers',
    20, 'EXTERNAL', 50, 21, false, false, false
    );



INSERT INTO seats (available, type, course_id)
VALUES (true, 'FRONT', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a');
INSERT INTO seats (available, type, course_id)
VALUES (true, 'FRONT', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a');
INSERT INTO seats (available, type, course_id)
VALUES (true, 'MIDDLE', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a');
INSERT INTO seats (available, type, course_id)
VALUES (true, 'BACK', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a');
INSERT INTO seats (available, type, course_id)
VALUES (true, 'BACK', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a');


INSERT INTO students (student_id,  student_name, username, department, semster, join_in)
VALUES ('5001', 'Nasruddin', 'nasruddin', 'Computer Science and Engineering', '4', '2015');
INSERT INTO students (student_id,  student_name, username, department, semster, join_in)
VALUES ('5002', 'User 1', 'user', 'Electronics And Communication', '5', '2012');
INSERT INTO students (student_id,  student_name, username, department, semster, join_in)
VALUES ('5003', 'admin', 'admin', 'Mechanical', '6', '2012');


INSERT INTO student_course (id, username,  course_id, status)
VALUES ('1', 'nasruddin', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78a', 'APPROVED');
INSERT INTO student_course (id, username,  course_id, status)
VALUES ('2', 'nasruddin', 'f3831f8c-c338-4ebe-a82a-e2fc1d1ff78b', 'PENDING');
