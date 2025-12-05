



import './Intro.css';

const Intro = () => {
    

    return(


        <body>

    <header>
        <ul>
            <li>About us</li>
            <li>What we offer</li>
            <li>Topics</li>
            <li>For teachers</li>
            <li>Sign in</li>
            <li>Register</li>
        </ul>
    </header>

    <main>

        <h1>QuizMe!</h1>
        <p>
            QuizMe! is an interactive platform designed to help students learn smarter through engaging,
            customizable quizzes. Whether you're preparing for school exams or university courses,
            QuizMe! lets you explore topic-based quizzes, track your progress, and review detailed performance statistics.
            You can even create your own quizzes to study more effectively or share with classmates. Learn at your own pace,
            test your knowledge, and make studying easier and more enjoyable with QuizMe!
        </p>
        <button>
            <p>Start Now!</p>
        </button>

        <div className="statistics">

            <div>
                    <h3>+200</h3>
                    <p>Are the institutions that choose us for the teaching 
                        of their subjects. From elementary schools to internatinal universities.
                    </p>
            </div>

           <span>

           </span>

            <div>
                    <h3>34</h3>
                    <p>Countries in 3 different continents have access to our app and tools. 
                        Our app goals for an international and intercultural scope

                    </p>
            </div>
            <span>
                
            </span>
            <div>
                    <h3>+9000</h3>
                    <p>Users learning with our quizzes around the world including
                         students, teachers, profesors and independent learners.
                    </p>
            </div>

        </div>

    </main>
    <footer>


    </footer>
</body>




    );
};

export default Intro;
