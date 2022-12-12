# Instructions to run Flocking-Evolver

Code described in the paper Evolving flocking in embodied agents based on local and global application of Reynolds’ rules: https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0224376

```
@article{ramos2019evolving,
  title={Evolving flocking in embodied agents based on local and global application of Reynolds’ rules},
  author={Ramos, Rita Parada and Oliveira, Sancho Moura and Vieira, Susana Margarida and Christensen, Anders Lyhne},
  journal={Plos one},
  volume={14},
  number={10},
  pages={e0224376},
  year={2019},
  publisher={Public Library of Science San Francisco, CA USA}
} 
```


The aim of this repo, Flocking-Evolver, is to evolve sef-organized flocking behaviour in the scope of Evolutionary Robotics. 

[![Demo CountPages alpha](https://j.gifs.com/p8gL7X.gif)](https://www.youtube.com/watch?v=ANGf4caCp9A)

# References, Installation and Guidance

1. Checkout both the JBotSim and JBotEvolver projects into your IDE
2. Set JBotSim is set as a dependency of JBotEvolver (if it's not done automatically already)
3. Run the CombinedGui class and check if the simulator runs
4. Run ritaMain.java in JBotRita and get into Configuration Page. Load file demo.conf and demo_global.conf under /JBotRita/MyExperiments to recur experiments with local and global setups respectively. Click 'Run Evolution' to start training.
5. After evolutions, click 'Load' to load corresponding output directories(/JBotRita/test and /JBotRita/testGlobal) in Results page, and then click 'Start/Pause' to observe results.  