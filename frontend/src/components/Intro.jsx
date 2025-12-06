




import './Intro.css';
import { useNavigate } from "react-router-dom";

const Intro = () => {
    


    const navigate = useNavigate();


    return(


        <div className = "intro-page">

    <header>
        <ul>
            <li><button>About us</button></li>
            <li><button>What we offer</button></li>
            <li><button>Topics</button></li>
            <li><button>For teachers</button></li>
            

            <li><button onClick={() => navigate("/auth" , { state: { showSignup: false } })}>Login</button></li>
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
        <button classNmae="start-btn"
        onClick={() => navigate("/auth" , { state: { showSignup: true } })}>
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
</div>




    );
};

export default Intro;