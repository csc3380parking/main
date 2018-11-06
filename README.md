#RIVER CROSSING- PACMAN STYLE
#######DEPENDANCIES################################################################################
import os, sys		                           # imports the standard python modules            
import pygame	                                   # imports the pygame package along with any modules belonging to pygame
import Tkinter	                                   # imports Tkinter module
import tkMessageBox                                # Imports Tkinter textbox modules
pygame.init()                                      # invokes pygame module
pygame.mixer.init()		                   # invokes mixer module (for music playback)

pygame.mixer.music.load('pmandubstep.ogg')             # load background music
pygame.mixer.music.play(0, 0)                      # set music playback to program initialisation

clock = pygame.time.Clock()                             # invokes time module from pygame (for setting framerate later on)
screen = pygame.display.set_mode((945, 690))		# Creates our Application Window at a certain resolution

RIGHTSIDE = 1        # Assigns value to the locations below depending on position (Right / Left of Bridge)           
LEFTSIDE = -1

ghostlocation = []                                      # Creates empty template for numbers above
pacmanloaction = []
fruitlocation = []
playerlocation = []

key_up = False	                                       # These default the button press variables to False (not pressed)
key_left = False
key_down = False
key_right = False

carryghost = False                                     # These are the variables for the picking up of the other objects by the player
carrypacman = False 
carryfruit = False


######IMPORTING OBJECTS############################################################################

background = pygame.image.load('pmanbackground2.gif').convert()    # imports the game background  
player = pygame.image.load('player.gif').convert()                # imports the player pic
screen.blit(background, (0, 0))                                   # draw the background screen
playerPosition = pygame.Rect(300,300,100,130)                     # set player position (start x, start y, width, height)
screen.blit(player, playerPosition)                               # draw the player marker

ghost = pygame.image.load('ghost.gif').convert()                  # import ghost pic
ghostPosition = pygame.Rect(150,50,100,130)                       # set ghost position
screen.blit(ghost, ghostPosition)                                 # draw the ghost

pacman = pygame.image.load('pacman.gif').convert()                # import pacman
pacmanPosition = pygame.Rect(150,325,75,130)                      # set pacman position
screen.blit(pacman, pacmanPosition) #draw Pacman                  # draw pacman

fruit = pygame.image.load('fruit.gif').convert()                  # import fruit pic
fruitPosition = pygame.Rect(150,585,75,100)                       # set fruit position
screen.blit(fruit, fruitPosition)                                 # draw the fruit


pygame.display.update()                                           # Update (display all elements)

######MAIN##########################################################################################
######QUIT COMMAND##################################################################################

while (True):                                                   # Start of while loop
  for event in pygame.event.get():                              # This creates indefinate loop to keep program running unless QUIT is invoked
    if event.type == pygame.QUIT:
      pygame.quit(); sys.exit();                	
######CONTROLS#######################################################################################

    elif event.type == pygame.KEYDOWN:                          # Start of KEYDOWN event (when a key is pressed)
      if event.key == pygame.K_RIGHT:                           # States if a key is pressed then the related variable becomes True
        key_right = True
      if event.key == pygame.K_LEFT:
	key_left = True
      if event.key == pygame.K_UP:
	key_up = True
      if event.key == pygame.K_DOWN:
	key_down = True
    elif event.type == pygame.KEYUP:                           # Start of KEYUP event (when a key is not pressed)
      if event.key == pygame.K_RIGHT:                          # States if a key is not pressed then the related variable becomes False 
        key_right = False 
      if event.key == pygame.K_LEFT:
        key_left = False 
      if event.key == pygame.K_UP:
        key_up = False 
      if event.key == pygame.K_DOWN:
        key_down = False

      if event.key == pygame.K_z:                             # States if z key is pressed then the ghost will be picked up
        if(carryghost == True):
          carryghost = False 
        elif(playerPosition.left <= ghostPosition.right and playerPosition.right >= ghostPosition.left and playerPosition.top <= ghostPosition.bottom and playerPosition.bottom >= ghostPosition.top):        
          carryghost = True
          carryfruit = False
          carrypacman = False


      if event.key == pygame.K_x:                            # States if x key is pressed then pacman will be picked up
        if(carrypacman == True):
          carrypacman = False
        elif(playerPosition.left <= pacmanPosition.right and playerPosition.right >= pacmanPosition.left and playerPosition.top <= pacmanPosition.bottom and playerPosition.bottom >= pacmanPosition.top): 
          carrypacman = True
          carryfruit = False
          carryghost = False


      if event.key == pygame.K_c:                            # States if c key is pressed then the fruit will be picked up
        if(carryfruit == True):
          carryfruit = False
        elif(playerPosition.left <= fruitPosition.right and playerPosition.right >= fruitPosition.left and playerPosition.top <= fruitPosition.bottom and playerPosition.bottom >= fruitPosition.top): 
          carryfruit = True
          carrypacman = False
          carryghost = False   
        
  screen.blit(background, playerPosition, playerPosition)  # erase
  screen.blit(background, ghostPosition, ghostPosition)    # erase
  screen.blit(background, pacmanPosition, pacmanPosition)  # erase
  screen.blit(background, fruitPosition, fruitPosition)    # erase
  
######MAP BOUNDARIES###############################################################################
   
  if playerPosition.right >= 910:
     playerPosition.right = 910
  if playerPosition.left <= 0:
     playerPosition.left = 0
  if playerPosition.bottom >= 715:
     playerPosition.bottom = 715
  if playerPosition.top <= 30:
     playerPosition.top = 30 

######PLAYER MOVEMENT###############################################################################

  if key_right == True:
    msElapsed = clock.tick(120)
    playerPosition = playerPosition.move(3, 0)
  if key_left == True:
    msElapsed = clock.tick(120)
    playerPosition = playerPosition.move(-3, 0) 
  if key_down == True:
    msElapsed = clock.tick(120)
    playerPosition = playerPosition.move(0, 3)
  if key_up == True:
    msElapsed = clock.tick(120)
    playerPosition = playerPosition.move(0, -3)
 
  
  if(carryghost == True): 
    ghostPosition = playerPosition
    
  if(carrypacman == True):
    pacmanPosition = playerPosition
     
  if(carryfruit == True):
    fruitPosition = playerPosition
    


######LOGIC SECTION##################################################################################

  if ghostPosition.left >= 650:
    ghostlocation = RIGHTSIDE
  elif ghostPosition.left < 650:
      ghostlocation = LEFTSIDE

  if pacmanPosition.left >= 650:
    pacmanlocation = RIGHTSIDE
  elif pacmanPosition.left < 650:
      pacmanlocation = LEFTSIDE

  if fruitPosition.left >= 650:
    fruitlocation = RIGHTSIDE
  elif fruitPosition.left < 650:
      fruitlocation = LEFTSIDE 

  if playerPosition.left >= 650:
    playerlocation = RIGHTSIDE
  elif playerPosition.left < 650:
      playerlocation = LEFTSIDE 

######GHOST & PACMAN SCENARIO#########################################################################

  if ((ghostlocation == LEFTSIDE) and (pacmanlocation == LEFTSIDE) and (fruitlocation == RIGHTSIDE) and (playerlocation == RIGHTSIDE) or (ghostlocation == RIGHTSIDE) and (pacmanlocation == RIGHTSIDE) and (fruitlocation == LEFTSIDE) and (playerlocation == LEFTSIDE)): 
    tkMessageBox.showinfo('GAME OVER', 'PacMan Was eaten by Ghost!')
    quit()

######PACMAN & FRUIT SCENARIO######################################################################### 

  if((ghostlocation == RIGHTSIDE) and (pacmanlocation == LEFTSIDE) and (fruitlocation == LEFTSIDE) and (playerlocation == RIGHTSIDE) or (ghostlocation == LEFTSIDE) and (pacmanlocation == RIGHTSIDE) and (fruitlocation == RIGHTSIDE) and (playerlocation == LEFTSIDE)): 
     tkMessageBox.showinfo('GAME OVER', 'PacMan ate the fruit!')
     quit()

######WINNING THE GAME################################################################################

  if ((ghostlocation == RIGHTSIDE) and (pacmanlocation == RIGHTSIDE) and (fruitlocation == RIGHTSIDE)):
    tkMessageBox.showinfo('SUCCESS!', 'You Completed the Game!')
    quit()      

######FINAL UPDATE####################################################################################
   
  screen.blit(player, playerPosition)
  screen.blit(ghost, ghostPosition)
  screen.blit(pacman, pacmanPosition)
  screen.blit(fruit, fruitPosition)
  pygame.display.update()

